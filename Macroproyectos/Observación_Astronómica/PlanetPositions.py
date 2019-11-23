import sys
import datetime
import math

planet_params = {}
planet_params['sun']     = {'N':[0,0],
                            'i':[0,0]}
planet_params['moon']    = {}
planet_params['mercury'] = {}
planet_params['venus']   = {}
planet_params['mars']    = {}
planet_params['jupiter'] = {}
planet_params['saturn']  = {}

planet_params['sun'] = {
    'N' : [0.0     , + 0.0         ],
    'i' : [0.0     , + 0.0         ],
    'w' : [282.9404, 4.70935E-5    ],
    'a' : 1.000000                  ,
    'e' : [0.016709, - 1.151E-9    ],
    'M' : [356.0470, + 0.9856002585] 
    }

planet_params['moon'] = {
    'N' : [125.1228, - 0.0529538083 ], 
    'i' : [5.1454  , + 0.0          ],
    'w' : [318.0634, + 0.1643573223 ],
    'a' : 60.2666                   ,
    'e' : [0.054900, + 0.0          ],
    'M' : [115.3654, + 13.0649929509] 
    }

planet_params['mercury'] = {
    'N' : [48.3313 , + 3.24587E-5  ],
    'i' : [7.0047  , + 5.00E-8     ],
    'w' : [29.1241 , + 1.01444E-5  ],
    'a' : 0.3870980                 ,
    'e' : [0.205635, + 5.59E-10    ],
    'M' : [168.6562, + 4.0923344368]  
    }

planet_params['venus'] = {
    'N' : [76.6799 , + 2.46590E-5   ],
    'i' : [3.3946  , + 2.75E-8      ],
    'w' : [54.8910 , + 1.38374E-5   ],
    'a' : 0.7233300                  ,
    'e' : [0.006773, - 1.302E-9     ],
    'M' : [48.0052 , + 1.6021302244 ] 
    }

planet_params['mars'] = {
    'N' : [49.5574 , + 2.11081E-5  ],
    'i' : [1.8497  , - 1.78E-8     ],
    'w' : [286.5016, + 2.92961E-5  ],
    'a' : 1.523688                  ,
    'e' : [0.093405, + 2.516E-9    ],
    'M' : [18.6021 , + 0.5240207766] 
    }

planet_params['jupiter'] = {
    'N' : [100.4542, + 2.76854E-5  ],
    'i' : [1.3030  , - 1.557E-7    ],
    'w' : [273.8777, + 1.64505E-5  ],
    'a' : 5.20256                   ,
    'e' : [0.048498, + 4.469E-9    ],
    'M' : [19.8950 , + 0.0830853001] 
    }

planet_params['saturn'] = {
    'N' : [113.6634, + 2.38980E-5  ],
    'i' : [2.4886  , - 1.081E-7    ],
    'w' : [339.3939, + 2.97661E-5  ],
    'a' : 9.55475                   ,
    'e' : [0.055546, - 9.499E-9    ],
    'M' : [316.9670, + 0.0334442282] 
    }

#####
#####DIA 1
#####
def timestamp_to_ellapsed_days(timestamp):
    timestamp-= 946598400 #This is the time stamp of Dec 31, 1999, 00:00:00 GMT
    return timestamp/86400.0

def get_orbital_params(planet, d):
    p = planet_params[planet]
    N =  (p['N'][0] + p['N'][1] * d)*math.pi/180
    i =  (p['i'][0] + p['i'][1] * d)*math.pi/180
    w =  (p['w'][0] + p['w'][1] * d)*math.pi/180
    a =   p['a']
    e =   p['e'][0] + p['e'][1] * d
    M = ((p['M'][0] + p['M'][1] * d)%360)*math.pi/180
    return [N, i, w, a, e, M]
#### FIN DE DIA 1


#####
#####DIA 2
#####
def calculate_eccentric_anomaly(M,e):
    E0 = M
    E1 = E0 - (M + e*math.sin(E0) - E0)/(e*math.cos(E0)-1)
    timeout = 100
    while abs(E1-E0) > 0.0000005 and timeout > 0:
        E0 = E1
        E1 = E0 - (M + e*math.sin(E0) - E0)/(e*math.cos(E0)-1)
        timeout -= 1
    if timeout == 0:
        print("WARNING! Eccentric anomaly did not converge.")
    return E1

def calculate_true_anomaly(E, e, a):
    x = a*(math.cos(E) - e)
    y = a*(math.sqrt(1-e*e)*math.sin(E))
    v = math.atan2(y,x)
    r = math.sqrt(x*x + y*y)
    return [v,r]
##### FIN DE DIA 2

#####
#####DIA 3
#####
def rotate_on_z(x, y, z, theta):
    xr = x*math.cos(theta) - y*math.sin(theta)
    yr = x*math.sin(theta) + y*math.cos(theta)
    zr = z
    return [xr, yr, zr]

def rotate_on_y(x, y, z, theta):
    xr =  x*math.cos(theta) + z*math.sin(theta)
    yr =  y
    zr = -x*math.sin(theta) + z*math.cos(theta)
    return [xr, yr, zr]

def rotate_on_x(x, y, z, theta):
    xr = x
    yr = y*math.cos(theta) - z*math.sin(theta)
    zr = y*math.sin(theta) + z*math.cos(theta)
    return [xr, yr, zr]

def calculate_ecliptic_xyz(r, N, w, v, i):
    x = r*math.cos(v)
    y = r*math.sin(v)
    z = 0
    [x,y,z] = rotate_on_z(x,y,z,w)
    [x,y,z] = rotate_on_x(x,y,z,i)
    [x,y,z] = rotate_on_z(x,y,z,N)
    return [x,y,z]

def calculate_equatorial_coords(xeclip, yeclip, zeclip):
    ECL = 23.4393*math.pi/180
    [xeq, yeq, zeq] = rotate_on_x(xeclip, yeclip, zeclip, ECL)
    RA  = math.atan2(yeq,xeq)
    Dec = math.atan2(zeq, math.sqrt(xeq*xeq + yeq*yeq))
    if RA < 0:
        RA += 2*math.pi
    return [RA, Dec]
##### FIN DE DIA 3

#####
#####DIA 4
#####
def get_sideral_local_time(M_sun, w_sun, lon, timestamp):
    secs = timestamp % 86400
    return  M_sun + w_sun + math.pi + lon + secs*2*math.pi/86400

def rad_to_HMS(a):
    H  = int(a*12/math.pi)
    a %= math.pi/12
    M  = int(a*720/math.pi)
    a %= math.pi/720
    S  = round(a*12*60*60/math.pi, 4)
    return "%02d:%02d:%02f"%(H,M,S)
##### FIN DE DIA 4

#####
#####DIA 5
#####

def get_azimuth_elevation(RA, Dec, LST, lat):
    HA = LST - RA
    x = math.cos(HA) * math.cos(Dec)
    y = math.sin(HA) * math.cos(Dec)
    z = math.sin(Dec)
    [xhor, yhor, zhor] = rotate_on_y(x,y,z, -(math.pi/2-lat))
    azimuth  = math.atan2( yhor, xhor ) + math.pi
    altitude = math.atan2( zhor, math.sqrt(xhor*xhor+yhor*yhor) )
    return [azimuth, altitude]

def rad_to_DMS(a):
    a *= 180/math.pi
    D = int(a)
    a %= 1
    a *= 60
    M = int(a)
    a %= 1
    S = a*60
    return "%02d°%02d'%02f"%(D,M,S)

def get_sun_position(lon, lat, timestamp):
    d = timestamp_to_ellapsed_days(timestamp)
    [N,i,w,a,e,M] = get_orbital_params('sun', d)
    E = calculate_eccentric_anomaly(M,e)
    [v,r] = calculate_true_anomaly(E, e, a)
    [xeclip, yeclip, zeclip] = calculate_ecliptic_xyz(r, N, w, v, i)
    [RA, Dec] = calculate_equatorial_coords(xeclip, yeclip, zeclip)
    LST = get_sideral_local_time(M, w, lon, timestamp)
    [Az,delta] = get_azimuth_elevation(RA, Dec, LST, lat)
    return [RA, Dec, Az, delta]


def get_moon_position(lon, lat, timestamp):
    d = timestamp_to_ellapsed_days(timestamp)
    [N,i,w_sun,a,e,M_sun] = get_orbital_params('sun', d)
    [N,i,w,a,e,M] = get_orbital_params('moon', d)
    E = calculate_eccentric_anomaly(M,e)
    [v,r] = calculate_true_anomaly(E, e, a)
    [xeclip, yeclip, zeclip] = calculate_ecliptic_xyz(r, N, w, v, i)
    [RA, Dec] = calculate_equatorial_coords(xeclip, yeclip, zeclip)
    LST = get_sideral_local_time(M_sun, w_sun, lon, timestamp)
    [Az,delta] = get_azimuth_elevation(RA, Dec, LST, lat)
    return [RA, Dec, Az, delta]

def get_planet_position(planet, lon, lat, timestamp):
    d = timestamp_to_ellapsed_days(timestamp)
    [N,i,w_sun,a,e,M_sun] = get_orbital_params('sun', d)
    E = calculate_eccentric_anomaly(M_sun,e)
    [v,r] = calculate_true_anomaly(E, e, a)
    [xe_sun, ye_sun, ze_sun] = calculate_ecliptic_xyz(r, N, w_sun, v, i)

    [N,i,w,a,e,M] = get_orbital_params(planet, d)
    E = calculate_eccentric_anomaly(M,e)
    [v,r] = calculate_true_anomaly(E, e, a)
    [xeclip, yeclip, zeclip] = calculate_ecliptic_xyz(r, N, w, v, i)

    xeclip += xe_sun
    yeclip += ye_sun
    zeclip += ze_sun
    [RA, Dec] = calculate_equatorial_coords(xeclip, yeclip, zeclip)
    LST = get_sideral_local_time(M_sun, w_sun, lon, timestamp)
    [Az,delta] = get_azimuth_elevation(RA, Dec, LST, lat)
    return [RA, Dec, Az, delta]
##### FIN DE DIA 5


if __name__  == "__main__":
    lon = -99.1332*math.pi/180
    lat =  19.4326*math.pi/180
    td  = datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S local time")
    ts  = datetime.datetime.now().timestamp()
    p   = "sun"
    for i in range(len(sys.argv)):
        if sys.argv[i] == "-d":
            td = sys.argv[i+1]
            ts = datetime.datetime.strptime(td, "%Y-%m-%dT%H:%M:%S%z").timestamp()
        elif sys.argv[i] == "--lon":
            lon = float(sys.argv[i+1])*math.pi/180
        elif sys.argv[i] == "--lat":
            lat = float(sys.argv[i+1])*math.pi/180
        elif sys.argv[i] == "--planet" or sys.argv[i] == "-p":
            p = sys.argv[i+1]
    print("Getting positions for " + p.upper() + " with:")
    print("Latitude  = " + rad_to_DMS(lat))
    print("Longitude = " + rad_to_DMS(lon))
    print("Date-Time = " + td)
    if p == 'sun':
        [RA, Dec, Az, Ele] = get_sun_position(lon, lat, ts)
    elif p == "moon":
        [RA, Dec, Az, Ele] = get_moon_position(lon, lat, ts)
    else:
        [RA, Dec, Az, Ele] = get_planet_position(p, lon, lat, ts)
    print("")
    print("The position of " + p.upper() + " is:")
    print("R. Ascention: " + rad_to_HMS(RA ))
    print("Declination:  " + rad_to_DMS(Dec))
    print("Azimuth:      " + rad_to_DMS(Az ))
    print("Elevation:    " + rad_to_DMS(Ele))
