package api.version_2_3_0.testdata;

import api.version_2_3_0.testdata.words.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomFoodBackedDataRetriever
                            implements TestDataRetriever {

    private final String [] projectNameSentences =
                                    {"gv cfn", "gv cu"};
    private final String [] todoNameSentences =
                                    {"gv cu", "gv fn", "gv vn",
                                    "fpv fn", "fpv fn", "fpv vn"};
    private Random r;

    public static final int MINIMUM_NUMBER_OF_PROJECTS = 10;
    public static final int MAXIMUM_NUMBER_OF_PROJECTS = 100;

    public static final int MINIMUM_NUMBER_OF_TODOS = 3;
    public static final int MAXIMUM_NUMBER_OF_TODOS = 20;

    public RandomFoodBackedDataRetriever(){
        this.r = new Random();
    }

    public List<Project> getProjects(){

        List<String> projectNames;

        projectNames = getRandomListFrom(projectNameSentences,
                MINIMUM_NUMBER_OF_PROJECTS,
                MAXIMUM_NUMBER_OF_PROJECTS);

        reportListAs(projectNames, "Projects");

        List<Project> projects = new ArrayList<Project>();
        for(String projectName : projectNames){
            Project aProject = new Project(projectName, projectName);
            projects.add(aProject);
        }

        return projects;
    }

    public List<Todo> getTodosForProject(String anID){

        List<String> todoNames;

        todoNames = getRandomListFrom(  todoNameSentences,
                MINIMUM_NUMBER_OF_TODOS,
                MAXIMUM_NUMBER_OF_TODOS);

        reportListAs(todoNames, "ToDos");

        List<Todo> todos = new ArrayList<>();
        for(String todoName : todoNames){
            Todo aTodo = new Todo(todoName, todoName);
            todos.add(aTodo);
        }

        return todos;
    }

    private void reportListAs(List<String> names, String title) {
        System.out.println(title);
        System.out.println("======");

        for(String name : names){
            System.out.println("- " + name + "(" + name +")");
        }
    }

    private List<String> getRandomListFrom( String []sentences,
                                            int minimumNumber,
                                            int maximumNumber) {

        List<String> names = new ArrayList<>();

        int numberOfItemsToGenerate =
                    r.nextInt(maximumNumber-minimumNumber) +
                                minimumNumber;

        for(int itemNumber = 0;
                itemNumber < numberOfItemsToGenerate; itemNumber++){
            String randomSentenceTemplate =
                        sentences[r.nextInt(sentences.length)];
            String randomName =
                        buildSentenceFromTemplate(randomSentenceTemplate);
            names.add(randomName);
        }

        return names;
    }

    private String buildSentenceFromTemplate(String aString){
        String terms[] = aString.split(" ");

        StringBuilder theSentence = new StringBuilder();

        for(String aTerm : terms){
            theSentence.append(getRandomStringFor(aTerm));
            theSentence.append(" ");
        }

        return theSentence.toString().trim();
    }


    private String getRandomStringFor(String aTerm) {

        RandomItemsArray itemsArray;

        switch(aTerm){
            case "gv":
                itemsArray = new GenericVerbs();
                break;
            case "fpv":
                itemsArray = new FoodPrepVerbs();
                break;
            case "cfn":
                itemsArray = new CompoundFoodNames();
                break;
            case "cu":
                itemsArray = new CookingUtensils();
                break;
            case "fn":
                itemsArray = new FruitNames();
                break;
            case "vn":
                itemsArray = new VegetableNames();
                break;
            default:
                System.err.println(
                        String.format("You forgot to add %s", aTerm));
                itemsArray = new GenericVerbs();
                break;
        }

        return itemsArray.
                getItems()[r.nextInt(itemsArray.getItems().length)];
    }



}