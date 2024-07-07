package version_2_3_0.testdata;


import api.version_2_3_0.testdata.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RandomDataGeneratorTest {

    @Test
    public void randomDataCreation(){

        Set<String> projectNames = new HashSet<>();

        RandomDataGenerator data = new RandomDataGenerator();

        for(int x=0; x<10; x++){
            String name = "Project" + data.randomWord();
            projectNames.add(name);
        }

        Assert.assertTrue("expected creation of more than one per millisecond", projectNames.size()<10);
        Assert.assertTrue("expected some names to be created", projectNames.size()>0);

        Iterator<String> name= projectNames.iterator();
        while(name.hasNext()){
            System.out.println(name.next());
        };

    }
}