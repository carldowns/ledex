package dao;


import app.SupplierResource;
import com.google.common.base.Optional;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

import supplier.SupplierDAO;
import supplier.SupplierEntity;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonResource}.
 */
public class SupplierSourceTest {
    private static final SupplierDAO DAO = mock(SupplierDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SupplierResource(DAO))
            .build();

    private SupplierEntity supplier;

    @Before
    public void setup() {
        supplier = new SupplierEntity("S100", "China FooBar Electric");
    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void getSupplier() {

        when(DAO.getSupplierByID("S100")).thenReturn(supplier);

        SupplierEntity found = resources.client().resource("/supplier/find?supplierID=S100").get(SupplierEntity.class);
        assertThat(found.getId()).isEqualTo(supplier.getId());

        verify(DAO).getSupplierByID("S100");
    }

}