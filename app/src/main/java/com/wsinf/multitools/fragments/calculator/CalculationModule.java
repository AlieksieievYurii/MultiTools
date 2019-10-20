package com.wsinf.multitools.fragments.calculator;

class CalculationModule {
    private final DistanceMetricSystem distanceMetricSystem;
    private final float distance;
    private final float fuelConsumption;
    private final float costFuel;
    private final float fuelSpent;
    private final float moneySpent;

    private CalculationModule(final DistanceMetricSystem distanceMetricSystem,
                              final float distance,
                              final float fuelConsumption,
                              final float costFuel,
                              final float fuelSpent,
                              final float moneySpent) {
        this.distanceMetricSystem = distanceMetricSystem;
        this.distance = distance;
        this.fuelConsumption = fuelConsumption;
        this.costFuel = costFuel;
        this.fuelSpent = fuelSpent;
        this.moneySpent = moneySpent;
    }

    float getDistance() {
        return distance;
    }

    float getFuelConsumption() {
        return fuelConsumption;
    }

    float getCostFuel() {
        return costFuel;
    }

    float getFuelSpent() {
        return fuelSpent;
    }

    float getMoneySpent() {
        return moneySpent;
    }

    DistanceMetricSystem getDistanceMetricSystem() {
        return distanceMetricSystem;
    }

    /**
     * Creates new instance of class CalculationModule
     * which contains result of calculation consumption
     *
     * @param distance    the distance of road
     * @param consumption the consumption of fuel
     * @param costFuel    cost of fuel
     * @return new object instance of CalculationModule which contains result of calculation consumption
     **/
    static CalculationModule calculate(final DistanceMetricSystem distanceMetricSystem,
                                       final float distance,
                                       final float consumption,
                                       final float costFuel) {
        final float spentFlue = distance * consumption;
        final float spentMoney = spentFlue * costFuel;

        return new CalculationModule(distanceMetricSystem,
                distance,
                consumption,
                costFuel,
                spentFlue,
                spentMoney);
    }
}


