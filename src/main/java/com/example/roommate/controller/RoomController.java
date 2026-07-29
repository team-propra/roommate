package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.annotations.VerifiedOnly;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.forms.RoomDataForm;
import com.example.roommate.values.forms.SearchTimeForm;
import com.example.roommate.values.models.RoomSearchModel;
import com.example.roommate.values.models.WorkspaceDetailsModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService & AdminApplicationService are properly injected")
public class RoomController {
    private static final String GUEST_HANDLE = "__guest__";
    private static final String SEARCH_FILTER_COOKIE = "roommate_search_filter";
    private static final int SEARCH_FILTER_COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

    private final BookingApplicationService bookingApplicationService;

    private final AdminApplicationService adminApplicationService;
    private final MessageSource messageSource;

    @Autowired
    public RoomController(BookingApplicationService bookingApplicationService, AdminApplicationService adminApplicationService, MessageSource messageSource) {
        this.bookingApplicationService = bookingApplicationService;
        this.adminApplicationService = adminApplicationService;
        this.messageSource = messageSource;
    }


    @GetMapping("/rooms")
    public String changeBookings(@RequestParam(required = false) List<String> gegenstaende, SearchTimeForm timeForm, Model model,
                                 OAuth2AuthenticationToken auth, HttpServletRequest request, HttpServletResponse response) {
        String userHandle = GUEST_HANDLE;
        if (auth != null) {
            OAuth2User user = auth.getPrincipal();
            userHandle = user.getAttribute("login");
        }

        if (hasSubmittedSearchFilter(request)) {
            saveSearchFilter(response, gegenstaende, timeForm);
        } else {
            SavedSearchFilter savedSearchFilter = readSearchFilter(request);
            if (savedSearchFilter != null) {
                gegenstaende = savedSearchFilter.selectedItems();
                timeForm = savedSearchFilter.timeForm();
            }
        }

        RoomSearchModel searchModel = bookingApplicationService.getRoomSearchModel(gegenstaende, timeForm, userHandle);
        model.addAttribute("date", searchModel.date());
        model.addAttribute("startTime", searchModel.startTime());
        model.addAttribute("endTime", searchModel.endTime());
        model.addAttribute("items", searchModel.items());
        model.addAttribute("gegenstaende", searchModel.selectedItems());
        model.addAttribute("roomBookingModels", searchModel.roomBookingModels());
        return "rooms";
    }

    private static boolean hasSubmittedSearchFilter(HttpServletRequest request) {
        return request.getParameterMap().containsKey("datum")
                || request.getParameterMap().containsKey("startUhrzeit")
                || request.getParameterMap().containsKey("endUhrzeit")
                || request.getParameterMap().containsKey("gegenstaende");
    }

    private static void saveSearchFilter(HttpServletResponse response, List<String> gegenstaende, SearchTimeForm timeForm) {
        String cookieValue = encodeSearchFilter(gegenstaende, timeForm);
        Cookie cookie = new Cookie(SEARCH_FILTER_COOKIE, cookieValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(SEARCH_FILTER_COOKIE_MAX_AGE_SECONDS);
        response.addCookie(cookie);
    }

    private static SavedSearchFilter readSearchFilter(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> SEARCH_FILTER_COOKIE.equals(cookie.getName()))
                .findFirst()
                .map(RoomController::tryDecodeSearchFilter)
                .orElse(null);
    }

    private static SavedSearchFilter tryDecodeSearchFilter(Cookie cookie) {
        try {
            return decodeSearchFilter(cookie);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String encodeSearchFilter(List<String> gegenstaende, SearchTimeForm timeForm) {
        List<String> parts = new ArrayList<>();
        parts.add(cookiePart("datum", timeForm.datum()));
        parts.add(cookiePart("startUhrzeit", timeForm.startUhrzeit()));
        parts.add(cookiePart("endUhrzeit", timeForm.endUhrzeit()));
        for (String item : nullToEmpty(gegenstaende)) {
            parts.add(cookiePart("gegenstaende", item));
        }
        return String.join("&", parts);
    }

    private static SavedSearchFilter decodeSearchFilter(Cookie cookie) {
        Map<String, List<String>> values = new LinkedHashMap<>();
        for (String part : cookie.getValue().split("&")) {
            String[] keyValue = part.split("=", 2);
            if (keyValue.length != 2) {
                continue;
            }
            values.computeIfAbsent(decode(keyValue[0]), ignored -> new ArrayList<>()).add(decode(keyValue[1]));
        }

        SearchTimeForm timeForm = new SearchTimeForm(
                firstValue(values, "datum"),
                firstValue(values, "startUhrzeit"),
                firstValue(values, "endUhrzeit")
        );
        return new SavedSearchFilter(timeForm, values.getOrDefault("gegenstaende", List.of()));
    }

    private static String cookiePart(String key, String value) {
        return encode(key) + "=" + encode(value);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String firstValue(Map<String, List<String>> values, String key) {
        return values.getOrDefault(key, List.of()).stream().findFirst().orElse(null);
    }

    private static List<String> nullToEmpty(List<String> values) {
        return values == null ? List.of() : values;
    }

    private record SavedSearchFilter(SearchTimeForm timeForm, List<String> selectedItems) {
    }

    @AdminOnly
    @GetMapping("/rooms/add")
    public String addRoomForm() {
        return "addRooms";
    }

    @AdminOnly
    @PostMapping("/rooms/add")
    public String addRoom(RoomDataForm roomDataForm){
        adminApplicationService.addRoom(roomDataForm);
        return "addRooms";
    }

    @GetMapping("/room/{roomId}/workspace/{workspaceId}")
    public ModelAndView roomDetails(Model model, @PathVariable UUID roomId, @PathVariable UUID workspaceId, Locale locale) {
        try {
            WorkspaceDetailsModel workspaceDetails = bookingApplicationService.getWorkspaceDetailsModel(roomId, workspaceId, locale);
            model.addAttribute("workspaceDetails", workspaceDetails);
            model.addAttribute("frame", workspaceDetails.frame());
            model.addAttribute("itemStringList", workspaceDetails.selectedItems());
            model.addAttribute("notSelectedItems", workspaceDetails.notSelectedItems());

            ModelAndView modelAndView = new ModelAndView("workspaceDetails");
            modelAndView.setStatus(HttpStatus.OK);
            return modelAndView;
        } catch (NotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("not-found");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }

    @VerifiedOnly
    @PostMapping("/rooms")

    public ModelAndView addBooking(@Valid BookDataForm form
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , @RequestParam(value = "cell", defaultValue = "false") List<String> checkedDays
//             ,@RequestParam(value="box", defaultValue = "false")List<String> boxes
            , OAuth2AuthenticationToken auth
            , Locale locale
    ) throws ArgumentValidationException {
        
        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        }

        if (!bookingApplicationService.isBookingSelectionValid(form, checkedDays)) {
            UUID roomId = form.roomId();
            UUID workspaceId = form.workspaceId();
            String errorMessage = messageSource.getMessage("workspace.noSelection", null, locale);
            redirectAttributes.addFlashAttribute("formValidationErrorText", errorMessage);
            return new ModelAndView("redirect:/room/%s/workspace/%s".formatted(roomId,workspaceId));
        }

        OAuth2User user = auth.getPrincipal();
        String userHandle = user.getAttribute("login");

        try {
            bookingApplicationService.addBookEntry(form, checkedDays, userHandle);
        } catch (GeneralDomainException | NotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("bad-request");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("redirect:/");
    }
}
