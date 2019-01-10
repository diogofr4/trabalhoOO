package campominado;

import static java.lang.Math.ceil;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;


public class Pontuacao
{
    ArrayList<Tempo> melhoresTempos;
    
    int qtdJogos;
    int vitorias;
    int [] tempoAtual = new int [3];
    int tempoGeral;
    
    
    public Pontuacao()
    {
        qtdJogos = vitorias = 0;
        melhoresTempos = new ArrayList();
    }
    
    public void setTempoAtual(int t,int l){
        tempoAtual[l-1]=t;
    }
    
    public int getTempoAtual(int l){
        return tempoAtual[l-1];
    }
    
    public int getQtdJogos()
    {
        return qtdJogos;        
    }
    
    public int getVitorias()
    {        
        return vitorias;
    }   
        
    public void incVitorias()
    {
        vitorias++;
    }
    
    public void incQtdJogados()
    {
        qtdJogos++;
    }
    
    public void resetaPontuacao()
    {
        qtdJogos = vitorias = 0;
    }
    
    
    
    public ArrayList<Tempo> getMelhoresTempos()
    {
        return melhoresTempos;
    }
        
    
    public void addTempo(int tempo, String nome)
    {
        melhoresTempos.add(new Tempo(tempo,nome));
        Collections.sort(melhoresTempos,new TimeComparator()); 
        
        if(melhoresTempos.size() > 5)
            melhoresTempos.remove(melhoresTempos.size()-1);
    }
     
    public boolean populacao()
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String dbURL = Jogo.dbPath; 

            connection = DriverManager.getConnection(dbURL); 
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM PONTUACAO");

            while(resultSet.next()) 
            {
                qtdJogos = resultSet.getInt("QTD_JOGOS");
                vitorias = resultSet.getInt("VITORIAS");                               
            }

            resultSet.close();
            statement.close();

            
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM TEMPO");
            
            
            while(resultSet.next())
            {
                int time = resultSet.getInt("TEMPO");
                String nome = resultSet.getString("NOME");
                
                melhoresTempos.add(new Tempo(time,nome));
            }

            resultSet.close();
            statement.close();

            connection.close();            
            
            return true;
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
            return false;
        }
    }

    
    public void salvar()
    {
        Connection connection = null;
        PreparedStatement statement = null;
        

        try {
            String dbURL = Jogo.dbPath; 
            
            connection = DriverManager.getConnection(dbURL); 

            String template = "DELETE FROM PONTUACAO"; 
            statement = connection.prepareStatement(template);
            statement.executeUpdate();

            template = "DELETE FROM TEMPO"; 
            statement = connection.prepareStatement(template);
            statement.executeUpdate();
           
            template = "INSERT INTO PONTUACAO (QTD_JOGOS,VITORIAS) values (?,?)";
            statement = connection.prepareStatement(template);
            
            statement.setInt(1, qtdJogos);
            statement.setInt(2, vitorias);          
            
            statement.executeUpdate();

            template = "INSERT INTO TEMPO (TEMPO, NOME) values (?,?)";
            statement = connection.prepareStatement(template);
            

            for (int i = 0; i < melhoresTempos.size(); i++)
            {
                statement.setInt(1, melhoresTempos.get(i).getTempo());
                statement.setString(2, melhoresTempos.get(i).getNome());
                
                statement.executeUpdate();            
            }


            statement.close();

            connection.close();            
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
        }
        
    }

    //--------------------------------------------------//
    
    
    //---------------------------------------------------//
    public class TimeComparator implements Comparator<Tempo>
    {
        @Override
        public int compare(Tempo a, Tempo b) {
            if (a.getTempo() > b.getTempo())
                return 1;
            else if (a.getTempo() < b.getTempo())
                return -1;
            else
                return 0;
        }                        
    }

    //----------------------------------------------------------//
    public class Tempo{
        String nome;
        int tempo;
        
        public Tempo(int t, String n)
        {
            tempo = t;
            nome = n;
        }
        
        public String getNome()
        {
            return nome;
        }
        
        public int getTempo()
        {
            return tempo;
        }        
    }    
}
