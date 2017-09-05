import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jiayicheng on 17/8/10.
 */
public class BuildOrder {

    Project[] findBuilderOrder(String[] projects, String[][] dependencies) {
        Graph graph = buildgraph(projects, dependencies);
        return orderProjects(graph.getNodes());
    }

    Graph buildGraph(String[] projects, String[][] dependencies) {
        Graph graph = new Graph();
        for (String project : projects)
            graph.createNode(project);
        for (String[] dependency : dependencies) {
            String first = dependency[0];
            String second = dependency[1];
            graph.addEdge(first, second);
        }
        return graph;
    }

    Project[] orderProjects(ArrayList<Project> projects) {
        Project[] order = new Project[projects.size()];

        int endofList = addNonDependent(order, projects, 0);

        int toBeProcessed = 0;
        while (toBeProcessed < order.length) {
            Project current = order[toBeProcessed];
            if (current == null)
                return null;
        }

        ArrayList<Project> children = current.getChildren();
        for (Project child : children)
            child.decrementDependencies();
        endofList = addNonDependent(order, children, endofList);
        toBeProcessed++;
    }

    int addNonDependent(Project order, ArrayList<Project> projects, int offset) {
        for (Project project : projects) {
            if (project.getNumberDependencies() == 0) {
                order[offset] = project;
                offset++;
            }
        }
        return offset;
    }

    public class Graph {
        private ArrayList<Project> nodes = new ArrayList<Project>();
        private HashMap<String, Project> map = new HashMap<String, Project>();

        public Project getOrCreateNode(String name) {
            if (!map.containsKey(name)) {
                Project node = new Project(name);
                nodes.add(node);
                map.put(name, node);
            }
            return map.get(name);
        }


        public void addEdge(String startname, String endname) {
            Project start = getOrCreateNode(startname);
            Project end = getOrCreateNode(endname);
            start.addNeighbor(end);
        }

        public ArrayList<Project> getNodes() {
            return nodes;
        }
    }

    public class Project{
        private ArrayList<Project> children=new ArrayList<Project>();
        private HashMap<String,Project>map=new HashMap<String,Project>();
        private String name;
        private int dependencies=0;

        public Project(String n){name=n;}
        public void addNeighbor(Project node)
        {
            if(!map.containsKey(node.getName())){
                children.add(node);
                map.put(node.getName(),node);
                node.incrementDependencies();
            }
        }
        public void incrementDependencies(){dependencies++;}
        public void decrementDependencies(){dependencies--;}
        public String getName(){return name;}
        public ArrayList<Project> getChildren(){return children;}
        public int getNumberDependencies(){return dependencies;}
    }
}
