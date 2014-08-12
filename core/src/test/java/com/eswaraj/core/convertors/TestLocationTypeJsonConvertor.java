package com.eswaraj.core.convertors;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.web.dto.LocationTypeJsonDto;

public class TestLocationTypeJsonConvertor extends BaseNeo4jEswarajTest {

    LocationTypeJsonConvertor locationTypeJsonConvertor;

    @Override
    @Before
    public void init() {
        locationTypeJsonConvertor = new LocationTypeJsonConvertor();
    }
    @Test
    public void test01_convertToJsonBean() throws ApplicationException {
        List<LocationType> dbLocationTypes = new ArrayList<>();
        LocationType root = addOneLocationType(dbLocationTypes, 1L,"India", null);
        LocationType state = addOneLocationType(dbLocationTypes, 2L, "State", root);
        addOneLocationType(dbLocationTypes, 3L, "AC", state);
        addOneLocationType(dbLocationTypes, 4L, "PC", state);
        addOneLocationType(dbLocationTypes, 5L, "District", state);
        LocationTypeJsonDto locationTypeJsonDto = locationTypeJsonConvertor.convertToJsonBean(dbLocationTypes);
        System.out.println(locationTypeJsonDto);
        Assert.assertEquals(1, locationTypeJsonDto.getChildren().size());
        Assert.assertEquals(3, locationTypeJsonDto.getChildren().get(0).getChildren().size());
    }

    private LocationType addOneLocationType(List<LocationType> dbLocationTypes, Long id, String name, LocationType parentLocationType) {
        LocationType locationtype = new LocationType();
        locationtype.setId(id);
        locationtype.setName(name);
        locationtype.setParentLocationType(parentLocationType);
        if (parentLocationType == null) {
            locationtype.setRoot(true);
        }
        dbLocationTypes.add(locationtype);
        return locationtype;
    }
}
