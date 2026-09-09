package com.evolutionnext.jqwik.arbitraries;

import net.jqwik.api.*;

public class ParameterizeArbitraryTest {

    public enum Position {
        PITCHER,
        CATCHER,
        FIRST_BASE,
        SECOND_BASE,
        THIRD_BASE,
        SHORTSTOP,
        LEFT_FIELD,
        CENTER_FIELD,
        RIGHT_FIELD,
        DESIGNATED_HITTER
    }

    public enum League {
        AMERICAN,
        NATIONAL
    }


    public record Team(
        String name,
        String city,
        String state) {
    }


    public record Player(
        String firstName,
        String lastName,
        Team team,
        Position position) {
    }

    public Arbitrary<Team> nationalLeagueArbitrary() {
        return Arbitraries.of(
            Tuple.of("Atlanta Braves", "Atlanta", "Georgia"),
            Tuple.of("Miami Marlins", "Miami", "Florida"),
            Tuple.of("New York Mets", "New York", "New York"),
            Tuple.of("Philadelphia Phillies", "Philadelphia", "Pennsylvania"),
            Tuple.of("Washington Nationals", "Washington", "District of Columbia"),
            Tuple.of("Chicago Cubs", "Chicago", "Illinois"),
            Tuple.of("Cincinnati Reds", "Cincinnati", "Ohio"),
            Tuple.of("Milwaukee Brewers", "Milwaukee", "Wisconsin"),
            Tuple.of("Pittsburgh Pirates", "Pittsburgh", "Pennsylvania"),
            Tuple.of("St. Louis Cardinals", "St. Louis", "Missouri"),
            Tuple.of("Arizona Diamondbacks", "Phoenix", "Arizona"),
            Tuple.of("Colorado Rockies", "Denver", "Colorado"),
            Tuple.of("Los Angeles Dodgers", "Los Angeles", "California"),
            Tuple.of("San Diego Padres", "San Diego", "California"),
            Tuple.of("San Francisco Giants", "San Francisco", "California")
        ).map(tuple -> new Team(tuple.get1(), tuple.get2(), tuple.get3()));
    }

    public Arbitrary<Team> americanLeagueArbitrary() {
        return Arbitraries.of(
            Tuple.of("Boston Red Sox", "Boston", "Massachusetts"),
            Tuple.of("New York Yankees", "New York", "New York"),
            Tuple.of("Tampa Bay Rays", "St. Petersburg", "Florida"),
            Tuple.of("Toronto Blue Jays", "Toronto", "Ontario"),
            Tuple.of("Baltimore Orioles", "Baltimore", "Maryland"),
            Tuple.of("Chicago White Sox", "Chicago", "Illinois"),
            Tuple.of("Cleveland Guardians", "Cleveland", "Ohio"),
            Tuple.of("Detroit Tigers", "Detroit", "Michigan"),
            Tuple.of("Kansas City Royals", "Kansas City", "Missouri"),
            Tuple.of("Minnesota Twins", "Minneapolis", "Minnesota"),
            Tuple.of("Houston Astros", "Houston", "Texas"),
            Tuple.of("Los Angeles Angels", "Anaheim", "California"),
            Tuple.of("Oakland Athletics", "Oakland", "California"),
            Tuple.of("Seattle Mariners", "Seattle", "Washington"),
            Tuple.of("Texas Rangers", "Arlington", "Texas")
        ).map(tuple -> new Team(tuple.get1(), tuple.get2(), tuple.get3()));
    }


    public Arbitrary<Team> allLeaguesArbitrary() {
        return Arbitraries.oneOf(americanLeagueArbitrary(), nationalLeagueArbitrary());
    }

    public Arbitrary<Player> allLeaguesPlayerArbitraryOfPosition(Position... positions) {
        Arbitrary<String> firstNameArbitrary =
            Arbitraries.strings()
                .alpha()
                .ofMinLength(2).ofMaxLength(20);

        Arbitrary<String> lastNameArbitrary =
            Arbitraries.strings()
                .alpha()
                .ofMinLength(2).ofMaxLength(30);

        Arbitrary<Position> positionArbitrary = positions.length == 0 ?
            Arbitraries.of(Position.values()) :
            Arbitraries.of(positions);

        return Combinators.combine(
                firstNameArbitrary,
                lastNameArbitrary,
                allLeaguesArbitrary(),
                positionArbitrary)
            .as(Player::new);
    }

    @Provide
    public Arbitrary<Player> allLeaguesPlayerArbitraryOfPitchers() {
        return allLeaguesPlayerArbitraryOfPosition(Position.PITCHER);
    }

    @Provide
    public Arbitrary<Player> allLeaguesPlayerArbitraryOfOutfielders() {
        return allLeaguesPlayerArbitraryOfPosition(Position.LEFT_FIELD, Position.CENTER_FIELD, Position.RIGHT_FIELD);
    }

    @Provide
    public Arbitrary<Player> allLeaguesPlayerArbitraryOfAllPositions() {
        return allLeaguesPlayerArbitraryOfPosition();
    }

    @Property
    public void testAllLeaguesPlayerArbitraryOfPitcher(@ForAll("allLeaguesPlayerArbitraryOfPitchers") Player player) {
        System.out.printf("Player: %s%n", player);
    }

    @Property
    public void testAllLeaguesPlayerArbitraryOfOutfielders(@ForAll("allLeaguesPlayerArbitraryOfOutfielders") Player player) {
        System.out.printf("Player: %s%n", player);
    }

    @Property
    public void testAllLeaguesPlayerArbitraryOfAllPositions(@ForAll("allLeaguesPlayerArbitraryOfAllPositions") Player player) {
        System.out.printf("Player: %s%n", player);
    }
}
