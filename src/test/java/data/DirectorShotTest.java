package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bart.
 */
public class DirectorShotTest {
    DirectorShot shot;
    @Before
    public void initialize() {
        shot = new DirectorShot("directorshot-1", "A test director shot");
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("A test director shot", shot.getDescription());
    }

    @Test
    public void setDescription() throws Exception {
        shot.setDescription("testdescription");
        assertEquals("testdescription", shot.getDescription());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("directorshot-1", shot.getName());
    }

    @Test
    public void getInstance() throws Exception {
        assertEquals(0, shot.getInstance());
    }

    @Test
    public void setName() throws Exception {
        shot.setName("testname");
        assertEquals("testname", shot.getName());
    }

}