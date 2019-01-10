package campominado;


import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class Menu extends JFrame
{
    private JButton[][] buttons;

    private int linhas;
    private int colunas;
    private JLabel minasLabel;
    private int minas;
    private static int level = 1;
    private JLabel tempoPassadoLabel;
    private JLabel tempoPassadoGeralLabel;
    private JLabel levelLabel;
    private JLabel vidaLabel;
    private JLabel nomeLabel;
    private Thread cronometro;
    private int tempoPassado;
    private static int tempoPassadoGeral = 0;
    private boolean paraCronometro;
    
    private final String FRAME_TITLE = "Campo Minado";
    
    private int FRAME_WIDTH = 520;
    private int FRAME_HEIGHT = 550;
    private int FRAME_LOC_X = 430;
    private int FRAME_LOC_Y = 50;

    private Icon minaVermelha;
    private Icon mina;
    private Icon bandeira;
    private Icon celula;
    
    private JMenuBar barraMenu;
    private JMenu jogoMenu;
    private JMenuItem novoJogo;
    private JMenuItem estatisticas;
    private JMenuItem sair;

    
    
    //---------------------------------------------------------------//
    public Menu(int r, int c, int m, int v, String n)
    {                
        this.linhas = r;
        this.colunas = c;
        
        buttons = new JButton [linhas][colunas];

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(FRAME_TITLE);
        setLocation(FRAME_LOC_X, FRAME_LOC_Y);

        JPanel tabuleiro;        
        JPanel tmPanel;        
        JPanel pontuacaoPanel;
        
        tabuleiro = new JPanel();
        tabuleiro.setLayout(new GridLayout(linhas,colunas,0,0));
        
        for( int y=0 ; y<linhas ; y++ ) 
        {
            for( int x=0 ; x<colunas ; x++ ) 
            {
                buttons[x][y] = new JButton("");

                buttons[x][y].setName(Integer.toString(x) + "," + Integer.toString(y));
                buttons[x][y].setFont(new Font("Serif", Font.BOLD, 24));
                
                buttons[x][y].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

                tabuleiro.add(buttons[x][y]);
            }
        }       
        
        JPanel tempoPassadoPanel = new JPanel();
        tempoPassadoPanel.setLayout(new BorderLayout(10,0));
        
        this.tempoPassadoLabel = new JLabel ("Fase:0|Geral:"+tempoPassadoGeral , SwingConstants.CENTER);
        tempoPassadoLabel.setFont(new Font("Serif", Font.BOLD, 15));
                
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
        tempoPassadoLabel.setBorder(loweredetched);
        tempoPassadoLabel.setBackground(new Color(110,110,255));
        tempoPassadoLabel.setForeground(Color.white);
        tempoPassadoLabel.setOpaque(true);
        
        JLabel iT = new JLabel("",SwingConstants.CENTER);
        iT.setIcon(new ImageIcon(getClass().getResource("/imagens/relogio.png"))); 

        tempoPassadoPanel.add(iT, BorderLayout.WEST);
        tempoPassadoPanel.add(tempoPassadoLabel, BorderLayout.CENTER);
        tempoPassadoPanel.setOpaque(false);
        
        this.tempoPassado = 0;
        this.paraCronometro = true;

        
        JPanel minasPanel = new JPanel();
        minasPanel.setLayout(new BorderLayout(10,0));
        

        this.minasLabel = new JLabel ("0" , SwingConstants.CENTER);
        minasLabel.setFont(new Font("Serif", Font.BOLD, 15));
        minasLabel.setBorder(loweredetched);
        minasLabel.setBackground(new Color(110,110,255));
        minasLabel.setForeground(Color.white);
        
        minasLabel.setOpaque(true);
        setMinas(m);
        
        JLabel mT = new JLabel("", SwingConstants.CENTER);
        mT.setIcon(new ImageIcon(getClass().getResource("/imagens/mina.png")));

        minasPanel.add(minasLabel, BorderLayout.EAST);
        minasPanel.add(mT, BorderLayout.CENTER);
        minasPanel.setOpaque(false);
        
        
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BorderLayout(10,0));
        

        this.levelLabel = new JLabel ("0" , SwingConstants.CENTER);
        levelLabel.setFont(new Font("Serif", Font.BOLD, 15));
        levelLabel.setBorder(loweredetched);
        levelLabel.setBackground(new Color(110,110,255));
        levelLabel.setForeground(Color.white);
        
        levelLabel.setOpaque(true);
        setLevel(level);
        
        JLabel lT = new JLabel("", SwingConstants.CENTER);
        lT.setIcon(new ImageIcon(getClass().getResource("/imagens/level.png")));

        levelPanel.add(levelLabel, BorderLayout.CENTER);
        levelPanel.add(lT, BorderLayout.WEST);
        levelPanel.setOpaque(false);        
        
        JPanel vidaPanel = new JPanel();
        vidaPanel.setLayout(new BorderLayout(10,0));
        

        this.vidaLabel = new JLabel ("0" , SwingConstants.CENTER);
        vidaLabel.setFont(new Font("Serif", Font.BOLD, 15));
        vidaLabel.setBorder(loweredetched);
        vidaLabel.setBackground(new Color(110,110,255));
        vidaLabel.setForeground(Color.white);
        
        vidaLabel.setOpaque(true);
        setVida(v);
        
        JLabel vT = new JLabel("", SwingConstants.CENTER);
        vT.setIcon(new ImageIcon(getClass().getResource("/imagens/coracao.png")));

        vidaPanel.add(vidaLabel, BorderLayout.CENTER);
        vidaPanel.add(vT, BorderLayout.WEST);
        vidaPanel.setOpaque(false);       

        JPanel nomePanel = new JPanel();
        nomePanel.setLayout(new BorderLayout(10,0));
        

        this.nomeLabel = new JLabel (" " + n, SwingConstants.CENTER);
        nomeLabel.setFont(new Font("Serif", Font.BOLD, 15));
        nomeLabel.setBorder(loweredetched);
        nomeLabel.setBackground(new Color(110,110,255));
        nomeLabel.setForeground(Color.white);
        
        nomeLabel.setOpaque(true);       

        nomePanel.add(nomeLabel, BorderLayout.CENTER);
        nomePanel.setOpaque(false);       

        tmPanel = new JPanel();
        tmPanel.setLayout(new BorderLayout(10,10));
        
        tmPanel.add(tempoPassadoPanel, BorderLayout.WEST);
        tmPanel.add(levelPanel, BorderLayout.CENTER);
        tmPanel.add(minasPanel, BorderLayout.EAST);
        tmPanel.add(vidaPanel, BorderLayout.PAGE_START);
        tmPanel.add(nomePanel, BorderLayout.PAGE_END);
        tmPanel.setOpaque(false);

        barraMenu = new JMenuBar();
        
        jogoMenu = new JMenu("Jogo");
         
        novoJogo = new JMenuItem("   Novo Jogo");
        estatisticas = new JMenuItem("   Estatísticas");
        sair = new JMenuItem("   Sair");

        novoJogo.setName("Novo Jogo");
        estatisticas.setName("Estatísticas");
        sair.setName("Sair");

        jogoMenu.add(novoJogo);
        jogoMenu.add(estatisticas);
        jogoMenu.add(sair);
        
        barraMenu.add(jogoMenu);                        
        //----------------------------------------------------//
               
        
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(0,10));
        p.add(tabuleiro, BorderLayout.CENTER);
        p.add(tmPanel, BorderLayout.SOUTH);
    
 
        p.setBorder(BorderFactory.createEmptyBorder(60, 60, 14, 60));        
        p.setOpaque(false);
      
        
        setLayout(new BorderLayout());
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/imagens/2.jpg")));
        
        add(background);        
        
        background.setLayout(new BorderLayout(0,0));
        
        background.add(barraMenu,BorderLayout.NORTH);
        background.add(p, BorderLayout.CENTER);        
        
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagens/mina.png")));
               
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
	
    public void iniciaCronometro()
    {        
        paraCronometro = false;
        
        cronometro = new Thread() {
                @Override
                public void run()
                {
                    while(!paraCronometro)
                    {
                        tempoPassado++;
                        tempoPassadoGeral++;

                        tempoPassadoLabel.setText("Fase:" + tempoPassado + "|Geral:" + tempoPassadoGeral);

                        try{
                            sleep(1000); 
                        }
                        catch(InterruptedException ex){}
                    }
                }
        };                

       cronometro.start();
    }

    
    public void interrompeCronometro()
    {
        paraCronometro = true;
                
        try 
        {
            if (cronometro!= null)
                cronometro.join();
        } 
        catch (InterruptedException ex) 
        {

        }        
    }
    
    public void resetaCronometro()
    {
        tempoPassado = 0;
        tempoPassadoLabel.setText("Fase:" + tempoPassado + "|Geral:" + tempoPassadoGeral);        
    }

    public void setTempoPassado(int t)
    {
        tempoPassado = t;
        tempoPassadoLabel.setText("Fase:" + tempoPassado + "|Geral:" + tempoPassadoGeral);                
    }
    
    //-----------------------------------------------------------//
    
    
    public void iniciaJogo()
    {
        escondeTodos();
        habilitaTodos();
    }
    

    public void habilitaTodos()
    {
        for( int x=0 ; x<colunas ; x++ ) 
        {
            for( int y=0 ; y<linhas ; y++ ) 
            {
                buttons[x][y].setEnabled(true);
            }
        }
    }

    public void desabilitaTodos()
    {
        for( int x=0 ; x<colunas ; x++ ) 
        {
            for( int y=0 ; y<linhas ; y++ ) 
            {
                buttons[x][y].setEnabled(false);
            }
        }
    }

    public void escondeTodos()
    {
        for( int x=0 ; x<colunas ; x++ ) 
        {
            for( int y=0 ; y<linhas ; y++ ) 
            {
                buttons[x][y].setText("");                
                buttons[x][y].setBackground(new Color(0,103,200));
                buttons[x][y].setIcon(celula);                
            }
        }
    }

    
    public void setButtonListeners(Jogo jogo)
    {
        addWindowListener(jogo);

        for( int x=0 ; x<colunas ; x++ ) 
        {
            for( int y=0 ; y<linhas ; y++ ) 
            {
                buttons[x][y].addMouseListener(jogo);
            }
        }
        
       novoJogo.addActionListener(jogo);
       estatisticas.addActionListener(jogo);
       sair.addActionListener(jogo);

       novoJogo.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
       sair.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
       estatisticas.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));       
    }
    
    
    public JButton[][] getButtons()
    {
        return buttons;
    }
    
    public int getTempoPassado()
    {
        return tempoPassado;
    } 
    
    public int getTempoPassadoGeral(){
        return tempoPassadoGeral;
    }


    public static void setLook(String look)
    {
        try {

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (look.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
        } catch (Exception ex) { }            
    }

    //-------------------------------------------------------------//
    
    public void setMinas(int m)
    {
        minas = m;
        minasLabel.setText(Integer.toString(m));
    }
    
    public void setVida(int v){
        vidaLabel.setText(Integer.toString(v));
    }
    
    public void setLevel(int l){
        level = l;
        levelLabel.setText(Integer.toString(l));
    }
    
    public int getLevel(){
        return level;
    }
    
    public void incLevel(){
        this.level++;
        setLevel(level);
    }
    
    public void incMinas()
    {
        minas++;
        setMinas(minas);
    }
    
    public void decMinas()
    {
        minas--;
        setMinas(minas);
    }
    
    public int getMinas()
    {
        return minas;
    }
            
    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) 
    {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }    
    
    public void setIcones()
    {

        int bOffset = buttons[0][1].getInsets().left;
        int bWidth = buttons[0][1].getWidth();
        int bHeight = buttons[0][1].getHeight();
        
        ImageIcon d;
        
        d = new ImageIcon(getClass().getResource("/imagens/minavermelha.png"));                
        minaVermelha =   resizeIcon(d, bWidth - bOffset, bHeight - bOffset);        

        d = new ImageIcon(getClass().getResource("/imagens/mina.png"));                
        mina =   resizeIcon(d, bWidth - bOffset, bHeight - bOffset);        
        
        d = new ImageIcon(getClass().getResource("/imagens/bandeira.png"));                
        bandeira =   resizeIcon(d, bWidth - bOffset, bHeight - bOffset);        
        
        d = new ImageIcon(getClass().getResource("/imagens/celula.png"));                
        celula =   resizeIcon(d, bWidth - bOffset, bHeight - bOffset);        
                
        //-------------------------------------------------------//
        
    }
    
    public Icon getIconeMina()
    {
        return mina;
    }

    public Icon getIconeMinaVermelha()
    {
        return minaVermelha;
    }
    
    public Icon getIconeBandeira()
    {
        return bandeira;
    }
    
    public Icon getIconeCelula()
    {
        return celula;       
    }        
    
    
    //---------------------------------------------------------------------//
    public void setCorTexto(JButton b)
    {
        if (b.getText().equals("1"))
            b.setForeground(Color.blue);
        else if (b.getText().equals("2"))
            b.setForeground(new Color(76,153,0));
        else if (b.getText().equals("3"))
            b.setForeground(Color.red);
        else if (b.getText().equals("4"))
            b.setForeground(new Color(153,0,0));
        else if (b.getText().equals("5"))
            b.setForeground(new Color(153,0,153));
        else if (b.getText().equals("6"))
            b.setForeground(new Color(96,96,96));
        else if (b.getText().equals("7"))
            b.setForeground(new Color(0,0,102));
        else if (b.getText().equals("8"))
            b.setForeground(new Color(153,0,76));        
    }
    //------------------------------------------------------------------------//
    
    
}
