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
    public void initializeTest() {
        shot = new DirectorShot("directorshot-1", "A test director shot", 1, 2);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("A test director shot", shot.getDescription());
    }

    @Test
    public void setDescriptionTest() {
        shot.setDescription("testdescription");
        assertEquals("testdescription", shot.getDescription());
    }

    @Test
    public void getNameTest() {
        assertEquals("directorshot-1", shot.getName());
    }

    @Test
    public void getInstanceTest() {
        int oldInstance = shot.getInstance();
        DirectorShot newShot = new DirectorShot("name", "description", 1, 2);
        assertEquals(oldInstance + 1, newShot.getInstance());
    }

    @Test
    public void setNameTest() {
        shot.setName("testname");
        assertEquals("testname", shot.getName());
    }

}