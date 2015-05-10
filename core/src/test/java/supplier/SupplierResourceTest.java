package supplier;


import static org.fest.assertions.api.Assertions.assertThat;

import supplier.dao.SupplierSQL;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonResource}.
 */
public class SupplierResourceTest {
    private static final SupplierSQL DAO = mock(SupplierSQL.class);

//    @ClassRule
//    public static final ResourceTestRule resources = ResourceTestRule.builder()
//            .addResource(new SupplierResource(DAO))
//            .build();
//
//    private SupplierRec supplierRec;
//
//    @Before
//    public void setup() {
//        supplierRec = new SupplierRec("S100", "China FooBar Electric");
//    }
//
//    @After
//    public void tearDown() {
//        reset(DAO);
//    }

    // FIXME the interface changed
//    @Test
//    public void someTest() {
//
//        when(DAO.getSupplierRecByID("S100")).thenReturn(supplierRec);
//
//        // TODO: fix this interface to match the Cmd pattern
//        SupplierRec found = resources.client().app.resource("/supplier/find?supplierID=S100").get(SupplierRec.class);
//        assertThat(found.getSupplierID()).isEqualTo(supplierRec.getSupplierID());
//
//        verify(DAO).getSupplierRecByID("S100");
//    }

}