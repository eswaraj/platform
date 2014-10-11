package com.eswaraj.core.service.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.web.dto.CategoryDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl_Test extends BaseNeo4jEswarajTest {

    @Autowired
    private AppService appService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private PersonService personService;

    public static class PredefinedBeanPostProcessor implements BeanPostProcessor {
        public Mockery context = new JUnit4Mockery();
        public StringRedisTemplate mock = context.mock(StringRedisTemplate.class);

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if ("stringRedisTemplate".equals(beanName)) {
                return mock;
            } else {
                return bean;
            }
        }
    }

    /**
     * Create a category and then get it by getcategory Service
     * 
     * @throws ApplicationException
     */
    @Test
    public void test01_saveCategory() throws ApplicationException {

        final String categoryName = randomAlphaString(16);
        final String categoryDescription = randomAlphaString(128);
        CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
        CategoryDto savedCategory = appService.saveCategory(categoryDto);
        assertEqualCategories(categoryDto, savedCategory, false);

        CategoryDto dbCategory = appService.getCategoryById(savedCategory.getId());
        assertEqualCategories(categoryDto, dbCategory, false);
        assertEqualCategories(savedCategory, dbCategory, true);

    }


}
