package br.com.fiap.techfood.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void shouldCompareAddressesByValue() {
        Address first = new Address("Main Street", "123", "Sao Paulo", "01310100");
        Address second = new Address("Main Street", "123", "Sao Paulo", "01310100");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
