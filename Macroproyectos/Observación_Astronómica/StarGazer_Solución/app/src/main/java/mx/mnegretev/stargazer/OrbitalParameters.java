package mx.mnegretev.stargazer;

class OrbitalParameters {
    public static final int SUN = 0;
    public static final int MOON = 1;
    public static final int MERCURY=2;
    public static final int VENUS = 3;
    public static final int MARS = 4;
    public static final int JUPITER = 5;
    public static final int SATURN = 6;


    private static double[][][] orbital_params = new double[7][6][2];

    public static void InitializePlanets()
    {
        orbital_params[0] = new double[][]{
            {0.0     , + 0.0         },
            {0.0     , + 0.0         },
            {282.9404, 4.70935E-5    },
            {1.000000, 0             },
            {0.016709, - 1.151E-9    },
            {356.0470, + 0.9856002585} 
        };

        orbital_params[1] = new double[][]{
            {125.1228, - 0.0529538083 }, 
            {5.1454  , + 0.0          },
            {318.0634, + 0.1643573223 },
            {60.2666,    0            },
            {0.054900, + 0.0          },
            {115.3654, + 13.0649929509} 
        };

        orbital_params[2] = new double[][]{
            {48.3313 , + 3.24587E-5  },
            {7.0047  , + 5.00E-8     },
            {29.1241 , + 1.01444E-5  },
            {0.387098,   0           },
            {0.205635, + 5.59E-10    },
            {168.6562, + 4.0923344368}  
        };

        orbital_params[3] = new double[][]{
            {76.6799 , + 2.46590E-5   },
            {3.3946  , + 2.75E-8      },
            {54.8910 , + 1.38374E-5   },
            {0.723330,   0            },
            {0.006773, - 1.302E-9     },
            {48.0052 , + 1.6021302244 } 
        };

        orbital_params[4] = new double[][]{
            {49.5574 , + 2.11081E-5  },
            {1.8497  , - 1.78E-8     },
            {286.5016, + 2.92961E-5  },
            {1.523688,   0            },
            {0.093405, + 2.516E-9    },
            {18.6021 , + 0.5240207766} 
        };

        orbital_params[5] = new double[][]{
            {100.4542, + 2.76854E-5  },
            {1.3030  , - 1.557E-7    },
            {273.8777, + 1.64505E-5  },
            {5.20256,    0           },
            {0.048498, + 4.469E-9    },
            {19.8950 , + 0.0830853001} 
        };

        orbital_params[6] = new double[][]{
            {113.6634, + 2.38980E-5  },
            {2.4886  , - 1.081E-7    },
            {339.3939, + 2.97661E-5  },
            {9.55475 ,   0           },
            {0.055546, - 9.499E-9    },
            {316.9670, + 0.0334442282} 
        };
    }

    public static double LongitudeOfAscendingNode(int planet, double days)
    {
        /*
         * TODO:
         * Calculate longitude of the ascending node for the given planet and days
         */
        double N = (orbital_params[planet][0][0] + days*orbital_params[planet][0][1])*Math.PI/180;
        return N;
    }

    public static double Inclination(int planet, double days)
    {
        /*
         * TODO:
         * Calculate inclination for the given planet and days
         */
        double i = (orbital_params[planet][1][0] + days*orbital_params[planet][1][1])*Math.PI/180;
        return i;
    }

    public static double ArgumentOfPeriapsis(int planet, double days)
    {
        /*
         * TODO:
         * Calculate argument of periapsis for the given planet and days
         */
        double w = (orbital_params[planet][2][0] + days*orbital_params[planet][2][1])*Math.PI/180;
        return w;
    }

    public static double SemimajorAxis(int planet, double days)
    {
        /*
         * TODO:
         * Calculate semimajor axis for the given planet and days
         */
        double a = orbital_params[planet][3][0];
        return a;
    }

    public static double Eccentricity(int planet, double days)
    {
        /*
         * TODO:
         * Calculate eccentricity for the given planet and days
         */
        double e = orbital_params[planet][4][0] + days*orbital_params[planet][4][1];
        return e;
    }

    public static double MeanAnomaly(int planet, double days)
    {
        /*
         * TODO:
         * Calculate mean anomaly for the given planet and days
         */
        double M = orbital_params[planet][5][0] + days*orbital_params[planet][5][1];
        M %= 360;
        M *= Math.PI /180.0;
        return M;
    }
}
