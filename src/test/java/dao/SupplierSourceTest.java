package dao;


import app.SupplierResource;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;

import static org.fest.assertions.api.Assertions.assertThat;

import supplier.Supplier;
import supplier.SupplierSQL;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonResource}.
 */
public class SupplierSourceTest {
    private static final SupplierSQL DAO = mock(SupplierSQL.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SupplierResource(DAO))
            .build();

    private Supplier supplier;

    @Before
    public void setup() {
        supplier = new Supplier("S100", "China FooBar Electric");
    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    @Ignore
    public void getSupplier() {

        when(DAO.getSupplierByID("S100")).thenReturn(supplier);

        // TODO: fix this interface to match the Cmd pattern
        Supplier found = resources.client().resource("/supplier/find?supplierID=S100").get(Supplier.class);
        assertThat(found.getId()).isEqualTo(supplier.getId());

        verify(DAO).getSupplierByID("S100");
    }

}