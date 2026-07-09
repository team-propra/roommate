package com.example.roommate.xcepto.controller;

import org.xcepto.xceptoj.TransitionBuilder;
import org.xcepto.xceptoj.ssr.SsrXceptoAdapter;
import org.xcepto.xceptoj.ssr.builders.SsrAdapterBuilder;
import org.xcepto.xceptoj.ssr.builders.SsrStateBuilderIdentity;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.UUID;

public final class RoommateHttp {
    private final SsrXceptoAdapter browser;

    private RoommateHttp(SsrXceptoAdapter browser) {
        this.browser = browser;
    }

    public static RoommateHttp anonymousGuest(TransitionBuilder builder, URI baseUri) {
        return from(builder, baseUri, RoommateHttpClients.anonymousBrowser());
    }

    public static RoommateHttp admin(TransitionBuilder builder, URI baseUri) {
        return from(builder, baseUri, RoommateHttpClients.browserFor("admin", "admin"));
    }

    public static RoommateHttp verifiedBooker(TransitionBuilder builder, URI baseUri) {
        return from(builder, baseUri, RoommateHttpClients.browserFor("verified", "verified"));
    }

    private static RoommateHttp from(TransitionBuilder builder, URI baseUri, HttpClient client) {
        SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                .withBaseUrl(baseUri)
                .withHttpClient(client)
                .build();
        return new RoommateHttp(browser);
    }

    public SsrStateBuilderIdentity opensPublicDashboard() {
        return browser.get("/")
                .withCustomName("Guest opens the Roommate dashboard");
    }

    public SsrStateBuilderIdentity opensWorkspaceSearch() {
        return browser.get("/rooms")
                .withCustomName("Guest opens workspace search");
    }

    public SsrStateBuilderIdentity searchesForWorkspaces(String date, String startTime, String endTime, String item) {
        return browser.get("/rooms")
                .withCustomName("Guest searches available workspaces")
                .addQueryArgument(() -> new String[]{"datum", date})
                .addQueryArgument(() -> new String[]{"startUhrzeit", startTime})
                .addQueryArgument(() -> new String[]{"endUhrzeit", endTime})
                .addQueryArgument(() -> new String[]{"gegenstaende", item});
    }

    public SsrStateBuilderIdentity opensWorkspace(UUID roomId, UUID workspaceId) {
        return browser.get("/room/%s/workspace/%s".formatted(roomId, workspaceId))
                .withCustomName("Guest opens workspace details");
    }

    public SsrStateBuilderIdentity opensAddRoomForm() {
        return browser.get("/rooms/add")
                .withCustomName("Admin opens the add-room form");
    }

    public SsrStateBuilderIdentity opensStylesheet() {
        return browser.get("/css/roommate.css")
                .withCustomName("Browser requests the Roommate stylesheet");
    }

    public SsrStateBuilderIdentity opensKeymasterAccessRegistry() {
        return browser.get("/api/access")
                .withCustomName("Keymaster reads the access registry");
    }

    public SsrStateBuilderIdentity opensAdminCenter() {
        return browser.get("/edit")
                .withCustomName("Admin opens the administration center");
    }

    public SsrStateBuilderIdentity opensRoomInventory(UUID roomId) {
        return browser.get("/room/" + roomId)
                .withCustomName("Admin opens room inventory");
    }

    public SsrStateBuilderIdentity createsCatalogItem(String itemName) {
        return browser.post("/createItem")
                .withCustomName("Admin creates a catalog item")
                .withFormContent(Map.of("newItem", itemName));
    }

    public SsrStateBuilderIdentity deletesCatalogItem(String itemName) {
        return browser.post("/deleteItem/" + itemName)
                .withCustomName("Admin deletes a catalog item");
    }

    public SsrStateBuilderIdentity createsWorkspace(UUID roomId, String workspaceNumber) {
        return browser.post("/createWorkspace")
                .withCustomName("Admin creates a workspace in a room")
                .withFormContent(Map.of("roomIDCreate", roomId.toString(), "newWorkspace", workspaceNumber));
    }

    public SsrStateBuilderIdentity deletesWorkspace(UUID roomId, UUID workspaceId) {
        return browser.post("/deleteWorkspace/" + workspaceId)
                .withCustomName("Admin deletes a workspace from a room")
                .withFormContent(Map.of("roomIDDelete", roomId.toString()));
    }

    public SsrStateBuilderIdentity addsWorkspaceItem(UUID roomId, UUID workspaceId, String itemName) {
        return browser.post("/room/%s/workspace/%s/addItem/%s".formatted(roomId, workspaceId, itemName))
                .withCustomName("Admin adds equipment to a workspace");
    }

    public SsrStateBuilderIdentity removesWorkspaceItem(UUID roomId, UUID workspaceId, String itemName) {
        return browser.post("/room/%s/workspace/%s/removeItem/%s".formatted(roomId, workspaceId, itemName))
                .withCustomName("Admin removes equipment from a workspace");
    }

    public SsrStateBuilderIdentity submitsBookingSelection(UUID roomId, UUID workspaceId, int stepSize, String selectedCell) {
        return browser.post("/rooms")
                .withCustomName("Verified booker submits a workspace booking selection")
                .withFormContent(Map.of(
                        "roomId", roomId.toString(),
                        "workspaceId", workspaceId.toString(),
                        "stepSize", String.valueOf(stepSize),
                        "cell", selectedCell
                ));
    }

    public SsrStateBuilderIdentity submitsBookingSelectionWithoutWorkspace(UUID roomId, int stepSize, String selectedCell) {
        return browser.post("/rooms")
                .withCustomName("Verified booker submits an incomplete booking selection")
                .withFormContent(Map.of(
                        "roomId", roomId.toString(),
                        "stepSize", String.valueOf(stepSize),
                        "cell", selectedCell
                ));
    }
}
