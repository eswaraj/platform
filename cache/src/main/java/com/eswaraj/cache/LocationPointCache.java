package com.eswaraj.cache;

import java.util.Set;

import com.eswaraj.core.exceptions.ApplicationException;

public interface LocationPointCache {

    void attachPointToLocations(double x, double y, Set<Long> locations) throws ApplicationException;

    void dettachPointFromLocations(double x, double y, Set<Long> locations) throws ApplicationException;

    Set<Long> getPointLocations(double x, double y) throws ApplicationException;
}
