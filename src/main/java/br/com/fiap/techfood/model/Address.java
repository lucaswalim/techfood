package br.com.fiap.techfood.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_number")
    private String number;

    @Column(name = "address_city")
    private String city;

    @Column(name = "address_zip_code")
    private String zipCode;
}
