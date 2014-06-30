package com.eswaraj.cache.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.web.dto.ComplaintDto;

@ContextConfiguration(locations={"classpath:eswaraj-cache.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class TestComplaintCache extends BaseCacheTest {

	@Autowired ComplaintCache complaintCache;

	@Test 
	public void shouldGetComplaint() {
		ComplaintDto complaint = createComplaintDto();
		
		complaintCache.set("C.1", complaint);
		ComplaintDto returnedComplaint = complaintCache.get("C.1");

		assertNotNull("complaint is <null>", complaint);
		assertEquals( "complaint wasn't found" , complaint, returnedComplaint);
	}
	
	@Test
	public void shouldDeleteComplaint() {
		ComplaintDto complaint = createComplaintDto();
		
		complaintCache.set("C.1.d", complaint);
		ComplaintDto returnedComplaint = complaintCache.get("C.1.d");

		assertNotNull("complaint is <null>", returnedComplaint);
		assertEquals( "complaint wasn't found" , complaint, returnedComplaint);
		
		complaintCache.delete("C.1.d");
		returnedComplaint = complaintCache.get("C.1.d");
		assertNull("complaint is <null>", returnedComplaint);
	}
}
