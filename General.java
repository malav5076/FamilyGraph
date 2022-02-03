
package general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

class PersonRelation{
    String RelatedPerson;
    String RelationType;
    
    PersonRelation(String RelatedPerson,String RelationType){
       this.RelatedPerson=RelatedPerson;
       this.RelationType=RelationType;
    }
}

class Group{
    static ArrayList<Group> groups=new ArrayList<>();
    static int groupNumber=-1;
    Group(){
        groupNumber++;
    }
    ArrayList<Person> personList;
    HashMap<String,ArrayList<PersonRelation>> personRelation;
}

class Person{
    String userName;
    String emailId;
    int age;
    
    
    Person(String[]PersonDataList){
        userName=PersonDataList[0];
        emailId=PersonDataList[1];
        age=Integer.parseInt(PersonDataList[2]);
    }
    static void makePersonList(ArrayList<String> personDataList ){
        Group group=new Group();
        group.personList=new ArrayList<>();
        group.personRelation=new HashMap<>();
        Group.groups.add(group);
        String personDataListStringSplit[];
        for(int i=0;i<personDataList.size();i++){
            personDataListStringSplit=personDataList.get(i).split(",");    
            Person person=new Person(personDataListStringSplit);
            group.personList.add(person);
            group.personRelation.put(personDataListStringSplit[1],new ArrayList<>());
        }
    }
    
    static void makeRelations(ArrayList<String> relationDataList){
        String relationDataStringSplit[];
        for(int i=0;i<relationDataList.size();i++){
            relationDataStringSplit=relationDataList.get(i).split(",");
            Group.groups.get(Group.groupNumber).personRelation.get(relationDataStringSplit[0]).add(new PersonRelation(relationDataStringSplit[2],relationDataStringSplit[1]));
            Group.groups.get(Group.groupNumber).personRelation.get(relationDataStringSplit[2]).add(new PersonRelation(relationDataStringSplit[0],relationDataStringSplit[1]));
        }
        
    }
    static void validateRelationship(int numberOfPerson,String emailId,int groupNumber){
        if(Group.groups.get(groupNumber).personRelation.get(emailId).size()==numberOfPerson)
            System.out.println("The Number of Relations are Correct");
        else 
            System.out.println("The number of Relatins are not Correct");
    }
    
    static void showRelationship(int groupNumber){
        for(int i=0;i<Group.groups.get(groupNumber).personList.size();i++){
          String emailId=Group.groups.get(groupNumber).personList.get(i).emailId;
          ArrayList<PersonRelation> al=Group.groups.get(groupNumber).personRelation.get(emailId);
          System.out.println("The "+" "+emailId+" is having relations with ");
          for(int j=0;j<al.size();j++) System.out.println(al.get(j).RelatedPerson+" of "+al.get(j).RelationType+" ");
          System.out.println("");
         
      }
    }
    static int extendedSizeOfFamily(String emailId,int groupNumber){
        ArrayList<ArrayList<PersonRelation>> stack=new ArrayList<>();
        HashMap<String,Boolean> visited=new HashMap<>();
        int memberCount=0;
        int top=-1;
        int flag=0;
        top=top+1;
        stack.add(top,Group.groups.get(groupNumber).personRelation.get(emailId));
        visited.put(emailId, true);
        memberCount++;
        while(top!=-1){
            while(flag==0){
                flag=1;
                for(int i=0;i<stack.get(top).size()&&Group.groups.get(groupNumber).personRelation.get(stack.get(top).get(i).RelatedPerson)!=null;i++){
                        
                    if(stack.get(top).get(i).RelationType.equals("FAMILY")){
                        
                        if(visited.get(stack.get(top).get(i).RelatedPerson)==null){
                            top=top+1;
                            stack.add(top,Group.groups.get(groupNumber).personRelation.get(stack.get(top-1).get(i).RelatedPerson));
                            visited.put(stack.get(top-1).get(i).RelatedPerson,true);
                            i=stack.get(top-1).size();
                            memberCount++;
                            flag=0;
                        }     
                    }     
                }
            }
            flag=0;
            top=top-1;
        }    
    System.out.println("The extended family size of "+emailId+" is "+memberCount);
    return memberCount;
    }
 }




class Reader{
    
    static ArrayList<String> readCommaSeperatedValueFile(String filePath){
        ArrayList<String> personDataList=new ArrayList<>();
        try{
            File file=new File(filePath);
            FileReader fileReader=new  FileReader(file);
            BufferedReader bufferedReader=new  BufferedReader(fileReader);            
            String personDataString=bufferedReader.readLine();                        
            while(personDataString!=null){
                if(!(personDataString.equals(""))) personDataList.add(personDataString);
                personDataString=bufferedReader.readLine();               
            }        
            bufferedReader.close();
           }
        catch(Exception e){
            System.out.println(e);
        }
        return personDataList;
    }
}    

public class General {

    public static void main(String[] args) {
     
     ArrayList<String> readFile;
     readFile=Reader.readCommaSeperatedValueFile("C://Users//Dell//Documents//files//people.csv");
     Person.makePersonList(readFile);
     readFile=Reader.readCommaSeperatedValueFile("C://Users//Dell//Desktop//relationships.csv");
     Person.makeRelations(readFile);
     Person.showRelationship(0);
     readFile=Reader.readCommaSeperatedValueFile("C://Users//Dell//Documents//people2.csv");
     Person.makePersonList(readFile);
     readFile=Reader.readCommaSeperatedValueFile("C://Users//Dell//Documents//relationships2.csv");
     Person.makeRelations(readFile);
     Person.showRelationship(0);
     Person.showRelationship(1);
    }
}

    
    

