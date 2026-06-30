package br.com.fiap.techfood.application.dto;

public record AddressData(
        String street,
        String number,
        String city,
        String zipCode
) {
}
