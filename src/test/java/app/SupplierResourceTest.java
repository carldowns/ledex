package app;


import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;

import static org.fest.assertions.api.Assertions.assertThat;

import supplier.SupplierRec;
import supplier.SupplierSQL;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonResource}.
 */
public class SupplierResourceTest {
    private static final SupplierSQL DAO = mock(SupplierSQL.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SupplierResource(DAO))
            .build();

    private SupplierRec supplierRec;

    @Before
    public void setup() {
        supplierRec = new SupplierRec("S100", "China FooBar Electric");
    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    // FIXME the interface changed
//    @Test
//    public void getSupplier() {
//
//        when(DAO.getSupplierRecByID("S100")).thenReturn(supplierRec);
//
//        // TODO: fix this interface to match the Cmd pattern
//        SupplierRec found = resources.client().resource("/supplier/find?supplierID=S100").get(SupplierRec.class);
//        assertThat(found.getSupplierID()).isEqualTo(supplierRec.getSupplierID());
//
//        verify(DAO).getSupplierRecByID("S100");
//    }

}