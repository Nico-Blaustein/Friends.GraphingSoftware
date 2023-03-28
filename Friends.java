package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

    /**
     * Finds the shortest chain of people from p1 to p2.
     * Chain is returned as a sequence of names starting with p1,
     * and ending with p2. Each pair (n1,n2) of consecutive names in
     * the returned chain is an edge in the graph.
     * 
     * @param g Graph for which shortest chain is to be found.
     * @param p1 Person with whom the chain originates
     * @param p2 Person at whom the chain terminates
     * @return The shortest chain from p1 to p2. Null or empty array list if there is no
     *         path from p1 to p2
     */
    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) 
    {

        Queue <Person> qOP = new Queue<Person>();
        qOP.enqueue(g.members[g.map.get(p1)]);
        
        ArrayList <String> lOF = new ArrayList<String>();
        lOF.add(g.members[g.map.get(p1)].name);

        Queue <ArrayList<String>> cOF = new Queue <ArrayList<String>>();
        cOF.enqueue(lOF);

        boolean [] v = new boolean [g.members.length];

        while(!qOP.isEmpty())
        {
            ArrayList<String> l = cOF.dequeue();
            Person p = qOP.dequeue();
            v[g.map.get(p.name)] = true;
            //Friend fP = g.members[g.map.get(p.name)].first;
            
            for(Friend fP = g.members[g.map.get(p.name)].first; fP != null; fP = fP.next)//while(fP != null)
            {
                if(v[fP.fnum] == true)
                {
                    continue;
                }
                else if (v[fP.fnum] == false)
                {
                    ArrayList<String> nL = new ArrayList<String>(l); // l
                    nL.add(g.members[fP.fnum].name);

                    if(g.members[fP.fnum].name.equals(p2))
                    {
                        return nL;
                    }
                    cOF.enqueue(nL);
                    qOP.enqueue(g.members[fP.fnum]);
                }
            }
        }
        return null;
    }
    //I can traverse through people by using friendships! Friendships act as nodes in a LL
    /*
        Run BFS starting at p1 until I reach p2. From there, I can use the EdgeTo array from p2 to retrace
        footsteps back to p1, inserting all names onto the shortestChain ArrayList. This will be the
        ShortestChain, because BFS does things by lvls, thus the first time I reach it, will be the fastest route.
        Also, if BFS concludes fully, I return null! Every Vertex should have been traversed.
    */


    
    /**
     * Finds all cliques of students in a given school.
     * 
     * Returns an array list of array lists - each constituent array list contains
     * the names of all students in a clique.
     * 
     * @param g Graph for which cliques are to be found.
     * @param school Name of school
     * @return Array list of clique array lists. Null or empty array list if there is no student in the
     *         given school
     */
    public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
        boolean[] vA = new boolean[g.members.length];
        ArrayList<ArrayList<String>> lFC = new ArrayList<ArrayList<String>>(); // MAY NEED TO FIX
        int i = 0;
        while(i < g.members.length)//for(int i = 0; i < g.members.length; i++)
        {
            Person p = g.members[i];
            if(vA[i] == true || p.student == false)
            {
                i++;
                continue;
            }
            ArrayList<String> nC = new ArrayList<String>();
            c2(g, i, nC, vA, school);
            if(nC != null && nC.size() > 0)
            {
                lFC.add(nC);
            }
            
            else
            {
                i++;
                continue;
            }
            
            i++;
        }
        return lFC;
    }

    private static void c2(Graph g, int index, ArrayList<String> cL, boolean[] hV, String nOS)
    {
        if(hV[index] == false)
        {
            if(g.members[index].student == true)
            {
                if(g.members[index].school.equals(nOS))
                {
                    cL.add(g.members[index].name);
                }
            }
        }

        hV[g.map.get(g.members[index].name)] = true;
        for(Friend ho = g.members[index].first; ho != null; ho = ho.next) 
        {
            if(hV[ho.fnum] == false && g.members[ho.fnum].school != null)
            {
                if(g.members[ho.fnum].student == true)
                {
                    if(g.members[ho.fnum].school.equals(nOS))
                    {
                        c2(g, ho.fnum, cL, hV, nOS);
                    }
                }
            }
        }
    }
        
    
    /**
     * Finds and returns all connectors in the graph.
     * 
     * @param g Graph for which connectors needs to be found.
     * @return Names of all connectors. Null or empty array list if there are no connectors.
     */
    public static ArrayList<String> connectors(Graph g) 
    {

        ArrayList<String> cL = new ArrayList<String>();
        int[] nD = new int[g.members.length];
        int[] pN = new int[nD.length];
        boolean[] vA = new boolean[nD.length];
        int c = 0;

        int i = 0;
        while(i < nD.length) 
        {
            if(vA[i] == false)
            {
                sFD(pN, nD, g.members[i], cL, vA, c, g);
            }
            else
            {
                i++;
                continue;
            }
            i++;
        }
        int j = 0;
        while(j < cL.size()) 
        {
            int one = 1;
            if(fFLM(g, g.members[g.map.get(cL.get(j))]).size() == one)
            {
                cL.remove(j);
            }
            else
            {
                j++;
                continue;
            }
            j++;
        }
        return cL;
    }

    private static ArrayList<Person> fFLM(Graph g, Person p)
    {
        ArrayList<Person> fL = new ArrayList<Person>();
        for(Friend pter = p.first; pter != null; pter = pter.next) //while(pter != null)
        {
            fL.add(g.members[pter.fnum]);
        }
        return fL;
    }

    private static void sFD(int[] pN, int[] DFSnum, Person p, ArrayList<String> cL, boolean[] v, int c, Graph g)
    {
        //int cI = g.map.get(p.name);
        ArrayList<Person> fL = fFLM(g, p);
        pN[g.map.get(p.name)] = c;
        v[g.map.get(p.name)] = true;
        DFSnum[g.map.get(p.name)] = c;
        c = c + 1;
        int i = 0;
        while(i < fL.size())//for(int i = 0; i < fL.size(); i++)
        {
            if(v[g.map.get(fL.get(i).name)] == false)
            { 
                sFD(pN, DFSnum, fL.get(i), cL, v, c, g);
                if(pN[g.map.get(fL.get(i).name)] < DFSnum[g.map.get(p.name)]) 
                {
                    pN[g.map.get(p.name)] = Math.min(pN[g.map.get(p.name)], pN[g.map.get(fL.get(i).name)]);
                }
                else
                {
                    if(DFSnum[ g.map.get(p.name)/*cI*/] == 0 && i == fL.size() - 1 && !cL.contains(p.name))
                    {
                        cL.add(p.name);
                    }
                    else if (!cL.contains(p.name))
                    {
                        cL.add(p.name);
                    }
                }
            }
            else 
            {
                pN[g.map.get(p.name)] = Math.min(DFSnum[g.map.get(fL.get(i).name)], pN[g.map.get(p.name)]); 
            }
            i++;
        }
        return;
    }
