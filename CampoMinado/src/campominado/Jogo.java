package campominado;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.border.TitledBorder;
import campominado.Pontuacao.Tempo;

public class Jogo implements MouseListener, ActionListener, WindowListener
{
    public static String dbPath;

    private boolean rodando; 

    private Tabuleiro tabuleiro;

    private Menu menu;
    
    private Jogador jogador;
    
    private Pontuacao pontuacao;
    
        
    //------------------------------------------------------------------//        

    public Jogo()
    {
        String p = "";

        try 
        {
            p = new File(Jogo.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "\\db.accdb";
        }
        catch (URISyntaxException ex) 
        {
            System.out.println("Erro ao carregar database.");
        }

        dbPath =   "jdbc:ucanaccess://" + p;

 
        pontuacao = new Pontuacao();
        pontuacao.populacao();
        
        Menu.setLook("Nimbus");
        criaJogador();                 
        criaTabuleiro(1);
        
        this.menu = new Menu(tabuleiro.getLinhas(), tabuleiro.getColunas(), tabuleiro.getQtdMinas(), jogador.getVidas(), jogador.getNome());        
        this.menu.setButtonListeners(this);
                        
        this.rodando = false;
        
        menu.setVisible(true);
        
        menu.setIcones();        
        menu.escondeTodos();

    }
    
    //-------------------------------------------------//
    public void setButtonImages()
    {
        Celulas cells[][] = tabuleiro.getCells();
        JButton buttons[][] = menu.getButtons();
        
        for( int y=0 ; y<tabuleiro.getLinhas() ; y++ ) 
        {
            for( int x=0 ; x<tabuleiro.getColunas() ; x++ ) 
            {
                buttons[x][y].setIcon(null);
                
                if (cells[x][y].getConteudo().equals(""))
                {
                    buttons[x][y].setIcon(menu.getIconeCelula());
                }
                else if (cells[x][y].getConteudo().equals("F"))
                {
                    buttons[x][y].setIcon(menu.getIconeBandeira());
                    buttons[x][y].setBackground(Color.blue);	                    
                }
                else if (cells[x][y].getConteudo().equals("0"))
                {
                    buttons[x][y].setBackground(Color.lightGray);
                }
                else
                {
                    buttons[x][y].setBackground(Color.lightGray);                    
                    buttons[x][y].setText(cells[x][y].getConteudo());
                    menu.setCorTexto(buttons[x][y]);                                        
                }
            }
        }
    }
    //------------------------------------------------------------//
    
    
    
    public void criaJogador(){
         String nome = JOptionPane.showInputDialog(null, "Qual é o seu nome?","Campo Minado", JOptionPane.QUESTION_MESSAGE);
        if(nome==null)
            System.exit(0);
        jogador = new Jogador(nome);
    }       
    
    
    //------------------------------------------------------------//
    public void criaTabuleiro(int level)
    {     
        int mina = 0;
        int l = 0;
        int c = 0;
        if(level == 1){
            mina = 10;
            l = 9;
            c = 9;    
        }
        if(level == 2){
            mina = 40;
            l = 16;
            c = 16; 
        }
        if(level == 3){
            mina = 99;
            l = 26;
            c = 26; 
        }
        this.tabuleiro = new Tabuleiro(mina, l, c);        
    }
    

    //---------------------------------------------------------------//
    public void novoJogo()
    {                
        this.rodando = false;        
                            
        criaTabuleiro(menu.getLevel());
        
        menu.interrompeCronometro();
        menu.resetaCronometro();
        menu.dispose();
        this.menu = new Menu(tabuleiro.getLinhas(), tabuleiro.getColunas(), tabuleiro.getQtdMinas(), jogador.getVidas(), jogador.getNome());        
        this.menu.setButtonListeners(this);
                        
        this.rodando = false;
        
        menu.setVisible(true);
        
        menu.setIcones();        
        menu.escondeTodos();
        
    }
    //------------------------------------------------------------------------------//
    
    public void reiniciaJogo()
    {
        this.rodando = false;
        
        tabuleiro.resetaTabuleiro();
        
        menu.interrompeCronometro();
        menu.resetaCronometro();        
        menu.iniciaJogo();
        menu.setMinas(tabuleiro.getQtdMinas());
    }
        
    //------------------------------------------------------------------------------//    
    private void encerraJogo()
    {
        rodando = false;
        mostraTudo();
        pontuacao.salvar();
    }

    
    public void vitoria()
    {
        pontuacao.incVitorias();
        pontuacao.incQtdJogados();
        menu.interrompeCronometro();
        pontuacao.setTempoAtual(menu.getTempoPassado(), menu.getLevel());
        encerraJogo();
        //----------------------------------------------------------------//
        
        
        JDialog dialog = new JDialog(menu, Dialog.ModalityType.DOCUMENT_MODAL);
        JLabel message;
        if(menu.getLevel() < 3){
            message = new JLabel("Parabéns, você venceu esta dificuldade!", SwingConstants.CENTER);
        }
        else{
            message = new JLabel("Parabéns, você venceu o jogo!", SwingConstants.CENTER);
        }
        JPanel estatisticas = new JPanel();
        estatisticas.setLayout(new GridLayout(6,1,0,10));
        
        if(menu.getLevel()==3)
            pontuacao.addTempo(menu.getTempoPassadoGeral(), jogador.getNome());
                
        JLabel qtdJogos = new JLabel("  Quantidade de jogos:  " + pontuacao.getQtdJogos());
        JLabel vitorias = new JLabel("  Vitórias:  " + pontuacao.getVitorias());
        JLabel level1;
        if(menu.getLevel()==1 || menu.getLevel()>1)
            level1 = new JLabel("  Level 1:  " + pontuacao.getTempoAtual(1) + "s");
        else{
            level1 = new JLabel("  Level 1: Não concluído ");
        }
        JLabel level2;
        if(menu.getLevel()==2 || menu.getLevel()>2)
            level2 = new JLabel("  Level 2:  " + pontuacao.getTempoAtual(2) + "s");
        else{
            level2 = new JLabel("  Level 2: Não concluído ");
        }
        JLabel level3;
        if(menu.getLevel()==3)
            level3 = new JLabel("  Level 3:  " + pontuacao.getTempoAtual(3) + "s");
        else{
            level3 = new JLabel("  Level 3: Não concluído ");
        }
        
        estatisticas.add(qtdJogos);
        estatisticas.add(vitorias);
        estatisticas.add(level1);
        estatisticas.add(level2);
        estatisticas.add(level3);
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);        
        estatisticas.setBorder(loweredetched);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,2,10,0));
        
        JButton sair = new JButton("Sair");
        if(menu.getLevel()<3){
            menu.incLevel();
            JButton jogarNovamente = new JButton("Jogar novamente");
            jogarNovamente.addActionListener((ActionEvent e) -> {
                dialog.dispose();            
                novoJogo();
            });
            buttons.add(jogarNovamente);
        }
        
        sair.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            windowClosing(null);
        });        
                
        
        
        buttons.add(sair);
        
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20,20));
        c.add(message, BorderLayout.NORTH);
        c.add(estatisticas, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    dialog.dispose();
                    novoJogo();
            }
            }
        );

        dialog.setTitle("Vitória");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(menu);
        dialog.setVisible(true); 
        
    }
    
    public void derrota()
    {
        pontuacao.incQtdJogados();
        jogador.decVida();
        menu.setVida(jogador.getVidas());
        menu.interrompeCronometro();
        
        encerraJogo();
        
        //----------------------------------------------------------------//

        JDialog dialog = new JDialog(menu, Dialog.ModalityType.DOCUMENT_MODAL);
        JLabel mensagem;
        if(jogador.getVidas()==0)
            mensagem = new JLabel("Suas vidas acabaram. Boa sorte na próxima!", SwingConstants.CENTER);
        else
            mensagem = new JLabel("Você perdeu. Boa sorte na próxima!", SwingConstants.CENTER);
        JPanel estatisticas = new JPanel();
        estatisticas.setLayout(new GridLayout(5,1,0,10));
        
        JLabel qtdJogos = new JLabel("  Quantidade de jogos:  " + pontuacao.getQtdJogos());
        JLabel vitorias = new JLabel("  Vitórias:  " + pontuacao.getVitorias());
        JLabel level1;
        if(menu.getLevel()>1)
            level1 = new JLabel("  Level 1:  " + pontuacao.getTempoAtual(1) + "s");
        else{
            level1 = new JLabel("  Level 1: Não concluído ");
        }
        JLabel level2;
        if(menu.getLevel()>2)
            level2 = new JLabel("  Level 2:  " + pontuacao.getTempoAtual(2) + "s");
        else{
            level2 = new JLabel("  Level 2: Não concluído ");
        }
        JLabel level3;
        if(menu.getLevel()==3)
            level3 = new JLabel("  Level 3:  " + pontuacao.getTempoAtual(3) + "s");
        else{
            level3 = new JLabel("  Level 3: Não concluído ");
        }
        
        estatisticas.add(qtdJogos);
        estatisticas.add(vitorias);
        estatisticas.add(level1);
        estatisticas.add(level2);
        estatisticas.add(level3);
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);        
        estatisticas.setBorder(loweredetched);
        
        
        //--------BUTTONS----------//
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,3,2,0));
        
        JButton sair = new JButton("Sair");
        if(jogador.getVidas()>0){
            JButton reiniciar = new JButton("Reiniciar");
            JButton jogarNovamente = new JButton("Jogar novamente");
            reiniciar.addActionListener((ActionEvent e) -> {
                dialog.dispose();            
                reiniciaJogo();
            });        
            jogarNovamente.addActionListener((ActionEvent e) -> {
                dialog.dispose();            
                novoJogo();
            });        
            buttons.add(reiniciar);
            buttons.add(jogarNovamente);
        }
        

        
        sair.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            windowClosing(null);
        });        
        
        
        
        buttons.add(sair);
       
        
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20,20));
        c.add(mensagem, BorderLayout.NORTH);
        c.add(estatisticas, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    dialog.dispose();
                    novoJogo();
            }
            }
        );
        
        dialog.setTitle("Derrota");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(menu);
        dialog.setVisible(true);        
    }
    
    public void mostraPlacar()
    {
        //----------------------------------------------------------------//
                
        JDialog dialog = new JDialog(menu, Dialog.ModalityType.DOCUMENT_MODAL);

        //-----Ranking--------//
        
        JPanel melhoresTempos = new JPanel();
        melhoresTempos.setLayout(new GridLayout(5,1));
        
        ArrayList<Tempo> mTempos = pontuacao.getMelhoresTempos();
        
        for (int i = 0; i < mTempos.size(); i++)
        {
            JLabel t = new JLabel("  " + mTempos.get(i).getTempo() + "           " + mTempos.get(i).getNome());            
            melhoresTempos.add(t);
        }
        
        if (mTempos.isEmpty())
        {
            JLabel t = new JLabel("                               ");            
            melhoresTempos.add(t);
        }
        
        TitledBorder b = BorderFactory.createTitledBorder("Ranking");
        b.setTitleJustification(TitledBorder.LEFT);

        melhoresTempos.setBorder(b);
                
        //-----Estatisticas-----------//
        JPanel estatisticas = new JPanel();
        
        estatisticas.setLayout(new GridLayout(6,1,0,10));        
        
        JLabel qtdJogos = new JLabel("  Quantidade de jogos:  " + pontuacao.getQtdJogos());
        JLabel vitorias = new JLabel("  Vitórias:  " + pontuacao.getVitorias());
        JLabel level1;
        if(menu.getLevel()>1)
            level1 = new JLabel("  Level 1:  " + pontuacao.getTempoAtual(1) + "s");
        else{
            level1 = new JLabel("  Level 1: Não concluído ");
        }
        JLabel level2;
        if(menu.getLevel()>2)
            level2 = new JLabel("  Level 2:  " + pontuacao.getTempoAtual(2) + "s");
        else{
            level2 = new JLabel("  Level 2: Não concluído ");
        }
        JLabel level3;
        if(menu.getLevel()==3)
            level3 = new JLabel("  Level 3:  " + pontuacao.getTempoAtual(3) + "s");
        else{
            level3 = new JLabel("  Level 3: Não concluído ");
        }
        
        estatisticas.add(qtdJogos);
        estatisticas.add(vitorias);
        estatisticas.add(level1);
        estatisticas.add(level2);
        estatisticas.add(level3);
                        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);        
        estatisticas.setBorder(loweredetched);
        
        
        //--------BUTTONS----------//
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,2,10,0));
        
        JButton fechar = new JButton("Fechar");
        JButton resetar = new JButton("Resetar");

        
        fechar.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });        
        resetar.addActionListener((ActionEvent e) -> {
            ImageIcon duvida = new ImageIcon(getClass().getResource("/imagens/duvida.png"));      

            int opcao = JOptionPane.showOptionDialog(null, "Deseja resetar todas as estatísticas para zero?", 
                            "Resetar estatísticas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, duvida,null,null);

            switch(opcao) 
            {
                case JOptionPane.YES_OPTION:      

                    pontuacao.resetaPontuacao();
                    pontuacao.salvar();
                    dialog.dispose();
                    mostraPlacar();
                    break;

                case JOptionPane.NO_OPTION: 
                    break;
            }
        });        
        
        buttons.add(fechar);
        buttons.add(resetar);
        
        if (pontuacao.getQtdJogos() == 0)
            resetar.setEnabled(false);
        
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20,20));
        c.add(melhoresTempos, BorderLayout.WEST);
        c.add(estatisticas, BorderLayout.CENTER);        
        c.add(buttons, BorderLayout.SOUTH);
        
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.setTitle("Estatísticas Campo Minado");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(menu);
        dialog.setVisible(true);                        
    }
    
    //------------------------------------------------------------------------------//
	
        
    // Mostra tudo
    private void mostraTudo()
    {
        String solucao;
        
        Celulas cells[][] = tabuleiro.getCells();
        JButton buttons[][] = menu.getButtons();

        for (int x=0; x<tabuleiro.getColunas(); x++ ) 
        {
            for (int y=0; y<tabuleiro.getLinhas(); y++ ) 
            {
                solucao = cells[x][y].getConteudo();

                if( solucao.equals("") ) 
                {
                    buttons[x][y].setIcon(null);
                    
                    solucao = Integer.toString(cells[x][y].getQtdMinas());

                    if(cells[x][y].getMina()) 
                    {
                        solucao = "M";
                        
                        buttons[x][y].setIcon(menu.getIconeMina());
                        buttons[x][y].setBackground(Color.lightGray);                        
                    }
                    else
                    {
                        if(solucao.equals("0"))
                        {
                            buttons[x][y].setText("");                           
                            buttons[x][y].setBackground(Color.lightGray);
                        }
                        else
                        {
                            buttons[x][y].setBackground(Color.lightGray);
                            buttons[x][y].setText(solucao);
                            menu.setCorTexto(buttons[x][y]);
                        }
                    }
                }

                else if( solucao.equals("F") ) 
                {
                    if(!cells[x][y].getMina()) 
                    {
                        buttons[x][y].setBackground(Color.orange);
                    }
                    else
                        buttons[x][y].setBackground(Color.green);
                }
                
            }
        }
    }
    
    //--------------------------------------------------------------------------//
    
    public boolean verificaTermino()
    {
        boolean terminou = true;
        String solucao;

        Celulas cells[][] = tabuleiro.getCells();
        
        for( int x = 0 ; x < tabuleiro.getColunas() ; x++ ) 
        {
            for( int y = 0 ; y < tabuleiro.getLinhas() ; y++ ) 
            {

                solucao = Integer.toString(cells[x][y].getQtdMinas());
                
                if(cells[x][y].getMina()) 
                    solucao = "F";

                if(!cells[x][y].getConteudo().equals(solucao))
                {
                    terminou = false;
                    break;
                }
            }
        }

        return terminou;
    }

 
    private void verificaFim()
    {		
        if(verificaTermino()) 
        {            
            vitoria();
        }
    }
   
    //----------------------------------------------------------------------/
       

    public void encontraZeros(int xCo, int yCo)
    {
        int vizinhos;
        
        Celulas cells[][] = tabuleiro.getCells();
        JButton buttons[][] = menu.getButtons();

        for(int x = tabuleiro.fazerCoordenadaValidaX(xCo - 1) ; x <= tabuleiro.fazerCoordenadaValidaX(xCo + 1) ; x++) 
        {			
            for(int y = tabuleiro.fazerCoordenadaValidaY(yCo - 1) ; y <= tabuleiro.fazerCoordenadaValidaY(yCo + 1) ; y++) 
            {
                if(cells[x][y].getConteudo().equals("")) 
                {
                    vizinhos = cells[x][y].getQtdMinas();

                    cells[x][y].setConteudo(Integer.toString(vizinhos));

                    if (!cells[x][y].getMina())
                        buttons[x][y].setIcon(null);                        
                    
                    if(vizinhos == 0)
                    {                        
                        buttons[x][y].setBackground(Color.lightGray);
                        buttons[x][y].setText("");
                        encontraZeros(x, y);
                    }
                    else
                    {
                        buttons[x][y].setBackground(Color.lightGray);
                        buttons[x][y].setText(Integer.toString(vizinhos));
                        menu.setCorTexto(buttons[x][y]);                        
                    }
                }
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) 
    {
        if (rodando)
        {
            ImageIcon duvida = new ImageIcon(getClass().getResource("/imagens/duvida.png"));      

            Object[] opcao = {"Sim","Não"};

            int sair = JOptionPane.showOptionDialog(null, "Tem certeza que deseja encerrar o jogo?", 
                            "Novo Jogo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, duvida, opcao, opcao[1]);

            switch(sair) 
            {
                //save
                case JOptionPane.YES_OPTION:
                    
                    pontuacao.incQtdJogados();
                    pontuacao.salvar();
                    System.exit(0);
                    break;
                                    
                case JOptionPane.NO_OPTION:
                    
                    break;                  
            }
        }
        else
            pontuacao.salvar();
            System.exit(0);
    }
    
    //-----------------------------------------------------------------------//

    @Override
    public void actionPerformed(ActionEvent e) {        
        JMenuItem menuItem = (JMenuItem) e.getSource();

        if (menuItem.getName().equals("Novo Jogo"))
        {
            if (rodando)
            {
                ImageIcon duvida = new ImageIcon(getClass().getResource("/imagens/duvida.png"));      

                Object[] opcao = {"Sair e iniciar um novo jogo","Reiniciar","Continuar jogando"};
                
                int comecarNovo = JOptionPane.showOptionDialog(null, "O que fazer com o jogo atual?", 
                                "Novo Jogo", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, duvida, opcao, opcao[2]);

                switch(comecarNovo) 
                {
                    case JOptionPane.YES_OPTION:      

                        novoJogo();
                        pontuacao.incQtdJogados();
                        pontuacao.salvar();
                        break;

                    case JOptionPane.NO_OPTION: 
                        pontuacao.incQtdJogados();   
                        pontuacao.salvar();
                        reiniciaJogo();
                        break;
                    
                    case JOptionPane.CANCEL_OPTION: break;
                }
            }
        }
        
        else if (menuItem.getName().equals("Exit"))
        {
            windowClosing(null);
        }
        
        else
        {
            mostraPlacar();
        }        
    }
    
    
    //--------------------------------------------------------------------------//
        
    //Mouse Click Listener
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(!rodando)
        {
            menu.iniciaCronometro();
            rodando = true;
        }
        
        if (rodando)
        {

            JButton button = (JButton)e.getSource();

            String[] co = button.getName().split(",");

            int x = Integer.parseInt(co[0]);
            int y = Integer.parseInt(co[1]);

            boolean eMina = tabuleiro.getCells()[x][y].getMina();
            int vizinhos = tabuleiro.getCells()[x][y].getQtdMinas();

            if (SwingUtilities.isLeftMouseButton(e)) 
            {
                if (!tabuleiro.getCells()[x][y].getConteudo().equals("F"))
                {
                    button.setIcon(null);

                    if(eMina) 
                    {  
                        button.setIcon(menu.getIconeMinaVermelha());
                        button.setBackground(Color.red);
                        tabuleiro.getCells()[x][y].setConteudo("M");
                        derrota();
                    }
                    else 
                    {
                        tabuleiro.getCells()[x][y].setConteudo(Integer.toString(vizinhos));
                        button.setText(Integer.toString(vizinhos));
                        menu.setCorTexto(button);

                        if( vizinhos == 0 ) 
                        {
                            button.setBackground(Color.lightGray);
                            button.setText("");
                            encontraZeros(x, y);
                        } 
                        else 
                        {
                            button.setBackground(Color.lightGray);
                        }
                    }
                }
            }

            else if (SwingUtilities.isRightMouseButton(e)) 
            {
                if(tabuleiro.getCells()[x][y].getConteudo().equals("F")) 
                {   
                    tabuleiro.getCells()[x][y].setConteudo("");
                    button.setText("");
                    button.setBackground(new Color(0,110,140));

                    button.setIcon(menu.getIconeCelula());
                    menu.incMinas();
                }
                else if (tabuleiro.getCells()[x][y].getConteudo().equals("")) 
                {
                    tabuleiro.getCells()[x][y].setConteudo("F");
                    button.setBackground(Color.blue);	

                    button.setIcon(menu.getIconeBandeira());
                    menu.decMinas();
                }
            }

            verificaFim();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }    

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
