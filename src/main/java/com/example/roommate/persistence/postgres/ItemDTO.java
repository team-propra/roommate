package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;

public record ItemDTO(@Id String itemName) {
}
