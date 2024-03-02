package com.workintech.s18d4.dto;

import com.workintech.s18d4.repository.CustomerRepository;

public record AccountResponse(long id, String accountName, double moneyAmount, CustomerResponse customerResponse) {
}
