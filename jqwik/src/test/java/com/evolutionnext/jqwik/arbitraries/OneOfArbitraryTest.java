package com.evolutionnext.jqwik.arbitraries;
import net.jqwik.api.*;


/**
 * Test class demonstrating the use of jqwik's oneOf arbitrary to generate random countries
 * from different regions of the Americas.
 */
public class OneOfArbitraryTest {

    /**
     * Record representing a country with its name.
     *
     * @param name The name of the country
     */
    public record Country(String name) {
    }

    /**
     * Provides an arbitrary that generates South American country names.
     *
     * @return Arbitrary that generates Country objects with South American country names
     */
    Arbitrary<Country> southAmericanCountries() {
        return Arbitraries.of("Brazil", "Bolivia", "Peru", "Colombia",
            "Venezuela", "Chile", "Ecuador", "Guyana", "Uruguay",
            "Suriname", "Paraguay", "French Guiana", "Argentina").map(Country::new);
    }

    /**
     * Provides an arbitrary that generates North American country names.
     *
     * @return Arbitrary that generates Country objects with North American country names
     */
    Arbitrary<Country> northAmericanCountries() {
        return Arbitraries.of("United States", "Canada", "Mexico").map(Country::new);
    }

    /**
     * Provides an arbitrary that generates Caribbean country names.
     *
     * @return Arbitrary that generates Country objects with Caribbean country names
     */
    Arbitrary<Country> caribbeanCountries() {
        return Arbitraries.of("Cuba", "Jamaica", "Haiti", "Dominican Republic",
            "Puerto Rico", "Bahamas", "Trinidad and Tobago", "Barbados",
            "Saint Lucia", "Grenada").map(Country::new);
    }

    /**
     * Provides an arbitrary that generates Central American country names.
     *
     * @return Arbitrary that generates Country objects with Central American country names
     */
    Arbitrary<Country> centralAmericanCountries() {
        return Arbitraries.of("Guatemala", "Belize", "Honduras", "El Salvador",
            "Nicaragua", "Costa Rica", "Panama").map(Country::new);
    }


    /**
     * Combines all regional country arbitraries into one arbitrary that generates
     * countries from all regions of the Americas.
     *
     * @return Arbitrary that generates Country objects from all American regions
     */
    @Provide
    Arbitrary<Country> allAmericasCountries() {
        return Arbitraries.oneOf(
            northAmericanCountries(),
            southAmericanCountries(),
            caribbeanCountries(),
            centralAmericanCountries()
        );
    }

    /**
     * Property test that verifies the generation of countries from all regions
     * by printing each generated country name.
     *
     * @param country A randomly generated country from the Americas
     */
    @Property
    public void testAllAmericasCountries(@ForAll("allAmericasCountries") Country country) {
        System.out.printf("Country: %s%n", country.name());
    }
}
