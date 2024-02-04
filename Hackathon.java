import java.awt.*; import java.awt.event.*; import javax.swing.*;
import javax.swing.event.*; import javax.swing.table.*; import java.util.*;
import java.io.*; import java.net.*; import java.sql.*;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel; import org.jvnet.substance.SubstanceLookAndFeel; import javax.swing.Timer;


/**
* Summary description for NodeName
*
*/
public class MainServer extends JFrame
{


private JPanel contentPane;


public static DefaultTableModel defaultTableModel=new DefaultTableModel();
 
public static JTable jTable=new JTable(defaultTableModel); public JScrollPane jScrollPane =new JScrollPane(jTable);

public static DefaultTableModel defaultTableModel1=new DefaultTableModel(); public static JTable jTable1=new JTable(defaultTableModel1);
public JScrollPane jScrollPane1 =new JScrollPane(jTable1); private GetAddress addObj = new GetAddress();
private String ipAddr = null;
public MainServer() throws Exception
{
ipAddr = addObj.getIp(); initializeComponent();
this.setVisible(true); new MainRouter();
}
private void initializeComponent()
{
contentPane = (JPanel)this.getContentPane(); contentPane.setLayout(null);

defaultTableModel.addColumn("Source"); defaultTableModel.addColumn("Destination"); defaultTableModel.addColumn("Message"); defaultTableModel1.addColumn("NODENAME"); defaultTableModel1.addColumn("IPADDRESS"); defaultTableModel1.addColumn("PORTNO"); defaultTableModel1.addColumn("ENERGY TRUST"); defaultTableModel1.addColumn("COMMUNICATION TRUST"); defaultTableModel1.addColumn("RECOMMENDATION TRUST"); defaultTableModel1.addColumn("GROUPSINK");
 
defaultTableModel1.addColumn("KEY"); addComponent(contentPane, jScrollPane, 20,70,880,200);
addComponent(contentPane, jScrollPane1, 20,400,880,200); this.setTitle(" Server ");
this.setLocation(new Point(0, 0));
this.setSize(new Dimension(1000, 700)); this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
try
{
Timer timer = new Timer(0, new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
updateRecords();
}
});
timer.setDelay(3000); // delay for 30 seconds timer.start();
}
catch(Exception ex)
{
System.out.println("Exception"+ex);
}


}

/** Add Component Without a Layout Manager (Absolute Positioning) */ private void addComponent(Container container,Component c,int x,int y,int
width,int height)
{
c.setBounds(x,y,width,height);
 
container.add(c);
}
 



try
{
 
public void updateRecords()
{
 

DefaultTableModel model=(DefaultTableModel)jTable.getModel(); model.setRowCount(0);

DefaultTableModel model1=(DefaultTableModel)jTable1.getModel(); model1.setRowCount(0);

DBConnection dbObj = new DBConnection(); Connection con = dbObj.getConnection(); Statement st = con.createStatement();
ResultSet rs1 = st.executeQuery("select * from NodeInfo"); while(rs1.next())
{
Vector vc=new Vector(); vc.add(rs1.getString(1)); vc.add(rs1.getString(2)); vc.add(rs1.getString(3)); vc.add(rs1.getString(4)); vc.add(rs1.getString(5)); vc.add(rs1.getString(6)); vc.add(rs1.getString(7)); vc.add(rs1.getString(8)); defaultTableModel1.addRow(vc);

}
 
ResultSet rs = st.executeQuery("select * from clouddata"); while(rs.next())
{
Vector vc=new Vector(); vc.add(rs.getString(1)); vc.add(rs.getString(2)); vc.add(rs.getString(3)); defaultTableModel.addRow(vc);
}
}
catch(Exception e)
{
System.out.println("Exception"+e);
}
}


public static void main(String[] args)
{
JFrame.setDefaultLookAndFeelDecorated(true);


try { SubstanceLookAndFeel
.setCurrentTheme("org.jvnet.substance.theme.SubstanceAquaTheme"); SubstanceLookAndFeel
.setCurrentWatermark("org.jvnet.substance.watermark.SubstanceMetalWallWatermark"); SubstanceLookAndFeel
.setCurrentGradientPainter("org.jvnet.substance.painter.SpecularGradientPainter"); SubstanceLookAndFeel
.setCurrentButtonShaper("org.jvnet.substance.button.ClassicButtonShaper"); UIManager.setLookAndFeel(new SubstanceLookAndFeel());
 
new MainServer();
} catch (Exception e) { e.printStackTrace();
}
}
//= End of Testing =
}

Nodeserver.java:

import java.util.*; import java.net.*; import java.io.*;

public class NodeServer extends TimerTask
{


private String nodeName; private String ipAdd; private int portNo; ServerSocket serObj; Socket socObj; ObjectInputStream ois; ObjectOutputStream oos;
private GetAddress addObj = new GetAddress(); public NodeServer()
{
System.out.println("Empty constructor");
}
public NodeServer(String nodeName,String ipAdd,int portNo )
{
this.nodeName = nodeName;
 
this.ipAdd = ipAdd; this.portNo = portNo;
}
public void run()
{
try
{
serObj = new ServerSocket( portNo ); System.out.println("Server is Listening"); while( true )
{
socObj = serObj.accept();
ois = new ObjectInputStream(socObj.getInputStream()); oos = new ObjectOutputStream(socObj.getOutputStream()); String message = (String)ois.readObject();
if( message.equals("transmit"))
{
String source = (String)ois.readObject(); String destination = (String)ois.readObject();
ArrayList pathInfo = (ArrayList)ois.readObject(); String splitMessage = (String)ois.readObject(); if( destination.equals( nodeName ))
{
SinkNodeName.jTextField1.setText(source); SinkNodeName.jTextArea1.selectAll(); SinkNodeName.jTextArea1.replaceSelection(""); SinkNodeName.jTextArea1.append(splitMessage+"\n");

//System.out.println("Message from source"+source+"	>"+nodeName);
 
System.out.println("Source"+source); System.out.println("destination"+destination); System.out.println("PathInfo"+pathInfo); System.out.println("Splitmessage"+splitMessage);

String ipAddr = addObj.getIp();
Socket socObj = new Socket(ipAddr,2345);
oos = new ObjectOutputStream(socObj.getOutputStream()); oos.writeObject("Insert Cloud");
oos.writeObject(source); oos.writeObject(destination); oos.writeObject(splitMessage);



/*Socket socObj1 = new Socket( ipAddress,portNumber);
ObjectOutputStream oos1 = new ObjectOutputStream(socObj1.getOutputStream()); oos1.writeObject("transmit");
oos1.writeObject(source); oos1.writeObject( destination ); oos1.writeObject( pathInfo ); oos1.writeObject( splitMessage );*/

}
else
{


System.out.println("Message from source"+splitMessage); if( pathInfo.size() == 0)
{
System.out.println("No Elemenets Here After");
 
}
else{
String nextNode = (String)pathInfo.get(0); String ipAddress = (String)pathInfo.get(1);
int portNumber = Integer.parseInt((String)pathInfo.get(2)); pathInfo.remove(0);
pathInfo.remove(0); pathInfo.remove(0);
Socket socObj1 = new Socket( ipAddress,portNumber);
ObjectOutputStream oos1 = new ObjectOutputStream(socObj1.getOutputStream()); oos1.writeObject("transmit");
oos1.writeObject(source); oos1.writeObject( destination ); oos1.writeObject( pathInfo ); oos1.writeObject( splitMessage );
System.out.println("Message from sourcenn"+nodeName+"	>"+nextNode);
}
}
}
}
}
catch (Exception e)
{
e.printStackTrace();
}
}
}
 

Sink Node:


import java.awt.*; import java.awt.event.*; import javax.swing.*;
import javax.swing.event.*; import javax.swing.table.*; import java.util.*;
import java.io.*; import java.net.*;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;



/**
* Summary description for NodeName
*
*/
public class SinkNodeName extends JFrame
{
// Variables declaration
private JTabbedPane jTabbedPane1; private JPanel contentPane;
// 	
private JPanel jPanel1;
// 	
private JLabel jLabel1;
static JTextField jTextField1; static JTextArea jTextArea1;
 
private JScrollPane jScrollPane2; private JPanel jPanel2;
// 	
public static JTable jTable1; private JScrollPane jScrollPane1;



public static DefaultTableModel defaultTableModel=new DefaultTableModel(); public static JTable jTable4=new JTable(defaultTableModel);
public JScrollPane jScrollPane4 =new JScrollPane(jTable4);



private Mobility mobility = new Mobility();


private JPanel jPanel3;


// 	
private JLabel jLabel2; private JLabel jLabel3;
private JLabel jLabel4; public JLabel jLabel5; public JLabel jLabel6; public JLabel jLabel7; public JLabel jLabel8; public JLabel jLabel9; public JLabel jLabel10;

private JTextField jTextField2; private JComboBox jComboBox1;
private JComboBox jComboBox2;
 
private JTextArea jTextArea2; private JScrollPane jScrollPane3; private JButton jButton1;
private JButton jButton2; private JButton jButton3; private JButton jButton4;
private JButton jButton5; private JButton jButton6; private JButton jButton7;

public static JProgressBar jProgressBar;



private String nodeName;


private String ipAdd;


private int portNo;


ArrayList<String> avlNodes = new ArrayList<String>();


ArrayList<String> avlNodes1 = new ArrayList<String>();



Object[] ObjArr;


Object[] ObjArr1;



byte[] b = null;
 

String sfile;


private String destination = null;


private GetAddress addObj = new GetAddress();


private String ipAddr = null;


Vector<String> packets = new Vector<String>();


Object[] header =
{"Source","Destination","Routes","Energy","Communication","recommend"};


public static RoutingTable rtObj;
// 	
// End of variables declaration


Socket socObj; ObjectInputStream dis; ObjectOutputStream dos; String en=null;
String input2="";



public SinkNodeName( String node,String host,String port,String en ) throws Exception
{
//super();


nodeName = node;
 

ipAdd = host;


portNo = Integer.parseInt(port);





ipAddr = addObj.getIp();


avlNodes = getAvlNodes1( nodeName );


ObjArr = avlNodes.toArray();



System.out.println("NodeName"+nodeName); System.out.println("The IpAddress is"+ipAdd);



initializeComponent();
//
// TODO: Add any constructor code after initializeComponent call
//


this.setVisible(true);



}



private void initializeComponent()
 
{
NodeServer serObj = new NodeServer( nodeName,ipAdd,portNo );


java.util.Timer timer = new java.util.Timer();


timer.schedule(serObj,3000);


jTabbedPane1 = new JTabbedPane(); contentPane = (JPanel)this.getContentPane();
// 	
jPanel1 = new JPanel();
// 	
jLabel1 = new JLabel(); jTextField1 = new JTextField(); jTextArea1 = new JTextArea(); jScrollPane2 = new JScrollPane(); jPanel2 = new JPanel();
// 	
jTable1 = new JTable();


rtObj = new RoutingTable(); rtObj.setColumnIdentifiers( header ); jTable1.setModel( rtObj );
jScrollPane1 = new JScrollPane( jTable1 );


jTable4 = new JTable();
 
jPanel3 = new JPanel();


jLabel2 = new JLabel();
jLabel3 = new JLabel(); jLabel4 = new JLabel();

jLabel5 = new JLabel("No. of Time Recharge :"); jLabel6 = new JLabel("1");
jLabel7 = new JLabel("Remaining Energy :"); jLabel8 = new JLabel("95");
jLabel9 = new JLabel("Relay Transmission Power :"); jLabel10 = new JLabel("0.0");

jTextField2 = new JTextField(); jComboBox1 = new JComboBox();
jComboBox2 = new JComboBox( ObjArr);


jTextArea2 = new JTextArea(); jScrollPane3 = new JScrollPane(); jButton1 = new JButton();
jButton2 = new JButton();
jButton3 = new JButton(); jButton4 = new JButton();
jButton5 = new JButton(); jButton6 = new JButton(); jButton7 = new JButton();



jProgressBar = new JProgressBar(); jProgressBar.setValue(80);
 
jProgressBar.setStringPainted(true);


contentPane.setLayout(null);


jLabel1.setText("Sender");


jScrollPane2.setViewportView(jTextArea1);


jScrollPane1.setViewportView(jTable1);



jLabel2.setText("Destination");
//
// jLabel3
// jLabel3.setText("FilePath");
jLabel4.setText("Possible Path");








jScrollPane3.setViewportView(jTextArea2);


jButton1.setText("Browse"); jButton1.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e)
{
jButton1_actionPerformed(e);
}
 

});


jButton2.setText("Send");
jButton2.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e)
{
jButton2_actionPerformed(e);
}


});


jButton3.setText("Refresh"); jButton3.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e)
{
jButton3_actionPerformed(e);
}


});


jButton4.setText("Select Path"); jButton4.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e)
{
jButton4_actionPerformed(e);
}


});
jButton5.setText("Exit");
 
jButton5.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e)
{
jButton5_actionPerformed(e);
}


});





defaultTableModel.addColumn("NODENAME"); defaultTableModel.addColumn("IPADDRESS"); defaultTableModel.addColumn("PORTNO"); defaultTableModel.addColumn("ENERGY TRUST"); defaultTableModel.addColumn("COMMUNICATION TRUST"); defaultTableModel.addColumn("RECOMMENDATION TRUST");




//addComponent(contentPane,jLabel2, 70,60,69,26);
//addComponent(contentPane,jLabel3, 65,117,57,18);
//addComponent(jPanel1, jLabel4, 250,61,120,18);


//addComponent(contentPane,jLabel5, 420,540,200,18);


//addComponent(contentPane,jLabel6, 580,540,200,18);


//addComponent(contentPane,jLabel7, 420,570,200,18);


//addComponent(contentPane,jLabel8, 580,570,200,18);
 

//addComponent(contentPane,jLabel9, 420,600,200,18);


//addComponent(contentPane,jLabel10, 580,600,200,18);


//addComponent(contentPane,jTextField2, 136,111,274,27);
//addComponent(contentPane,jComboBox1, 138,61,100,26);
//addComponent(contentPane,jScrollPane3, 59,171,400,200);
//addComponent(contentPane,jButton1, 424,111,83,28);
//addComponent(contentPane, jButton2, 480,340,78,33);


addComponent(contentPane, jButton3, 580,340,77,35);


//addComponent(contentPane, jButton4, 680,340,77,35);


addComponent(contentPane, jButton5, 780,340,77,33);






//addComponent(contentPane, jProgressBar, 300, 60, 125, 25);





addComponent(contentPane, jLabel1, 20,60,60,18);
addComponent(contentPane, jTextField1, 70,57,116,27);
addComponent(contentPane, jScrollPane2, 20,110,800,200);


//addComponent(contentPane, jScrollPane1, 630,400,350,200);
addComponent(contentPane, jScrollPane4, 20,400,880,200);
 

//
// NodeName
//





this.setTitle(" SinkNode "+nodeName); this.setLocation(new Point(0, 0));
this.setSize(new Dimension(1000, 700)); this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//mobility.CallRandom(nodeName);



}

/** Add Component Without a Layout Manager (Absolute Positioning) */ private void addComponent(Container container,Component c,int x,int y,int
width,int height)
{

c.setBounds(x,y,width,height); container.add(c);
}






public void getallpath()
{
Socket socObj;
 
ObjectOutputStream oos;


ObjectInputStream ois;
ArrayList<String> allNodes = new ArrayList<String>();



try
{
socObj = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(socObj.getOutputStream());


oos.writeObject("getallnodes");


oos.writeObject( nodeName+"#"+destination );


ois = new ObjectInputStream(socObj.getInputStream());



clearTable(defaultTableModel);


allNodes = (ArrayList)ois.readObject();


System.out.println(allNodes);


for(int j = 0; j < allNodes.size();j=j+6)
{
Vector ds = new Vector(); ds.addElement(allNodes.get(j)); ds.addElement(allNodes.get(j+1));
 
ds.addElement(allNodes.get(j+2)); ds.addElement(allNodes.get(j+3)); ds.addElement(allNodes.get(j+4)); ds.addElement(allNodes.get(j+5));
//ds.addElement(allNodes.get(j+6));
//print(ds) if(allNodes.get(j).equals(nodeName))
{


System.out.println("Node Name :" + nodeName); int ff=Integer.parseInt(allNodes.get(j+3)); System.out.println("Energy :" + ff );
Double re=100.00-ff; System.out.println("Remaing Energy :" + re ); jLabel8.setText(String.valueOf(re));

jLabel6.setText(String.valueOf(allNodes.get(j+4)));


Double nk=Double.parseDouble(jLabel6.getText()); System.out.println("Nk value :" + nk );

if(ff != 5)
{
long currentTime	= System.currentTimeMillis()/1000;


System.out.println("currentTimes " + currentTime);


Double pk=nk + (re/currentTime);


jLabel10.setText(String.valueOf(pk));
 





System.out.println("pk value " + pk);


}


}


defaultTableModel.addRow(ds);
}










}
catch (Exception e)
{
e.printStackTrace();
}



}


private void jButton1_actionPerformed(ActionEvent e)
{
String fileContent = "";
 
try
{
File file;


JFileChooser jfc = new JFileChooser(".");


int val = jfc.showOpenDialog(this);


if( val == JFileChooser.APPROVE_OPTION )
{
file = jfc.getSelectedFile();


FileInputStream fis = new FileInputStream( file );


b = new byte[ fis.available()];


fis.read(b);



sfile = file.getName();


if(sfile.endsWith(".txt") || sfile.endsWith(".java"))
{


fileContent = new String(b);



}


else
 
{
fileContent = "File is Not in Text Format";


}


jTextArea2.setText( fileContent );


jTextField2.setText( sfile );


convertToPackets();
}



}
catch (Exception e1)
{
e1.printStackTrace();
}


}


private void jButton2_actionPerformed(ActionEvent e)
{


//randomSelectionProcess();


}


private void jButton3_actionPerformed(ActionEvent e)
{
 

//refresh


ArrayList<String> as = getAvlNodes( nodeName );


jComboBox1.removeAllItems();


jComboBox1.addItem(input2);





for(int i = 0; i < as.size();i++)
{



}



}
private void jButton4_actionPerformed(ActionEvent e)
{
selectDestination();


}
private void jButton5_actionPerformed(ActionEvent e)
{


exitNode();


System.exit(0);
 

}



public void exitNode()
{
Socket socObj;


ObjectOutputStream oos;


ObjectInputStream ois;


String paths = "";



try
{



//System.out.println("The Destination is"+destination);



if( true)
{


System.out.println(nodeName+"going to delete");



socObj = new Socket(ipAddr,2345);
 
oos = new ObjectOutputStream(socObj.getOutputStream());


oos.writeObject("delete");


oos.writeObject( nodeName);


JOptionPane.showMessageDialog(this,nodeName,"Logout",JOptionPane.INFORMATION
_MESSAGE);



}



}
catch (Exception e)
{
e.printStackTrace();
}



}






//
// TODO: Add any method code to meet your needs in the following area
//



public ArrayList<String> getAvlNodes(String currentNode)
 
{
Socket socObj;


ObjectInputStream ois;


ObjectOutputStream oos;


ArrayList<String> nodeNames = new ArrayList<String>();










try
{
//	GetAddress obj = new GetAddress();
//
//	String ipAddr = obj.getIp();


if( true )
{


socObj = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(socObj.getOutputStream());


oos.writeObject("getothernodes");


oos.writeObject( currentNode );
 

ois = new ObjectInputStream(socObj.getInputStream());


nodeNames = (ArrayList)ois.readObject();


getallpath();


}


}
catch (Exception e)
{
e.printStackTrace();
}


return nodeNames;
}


public ArrayList<String> getAvlNodes1(String currentNode)
{
Socket socObj;


ObjectInputStream ois;


ObjectOutputStream oos;


ArrayList<String> nodeNames = new ArrayList<String>();


try
{
 
//	GetAddress obj = new GetAddress();
//
//	String ipAddr = obj.getIp();


if( true )
{


socObj = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(socObj.getOutputStream());


oos.writeObject("getothernodes");


oos.writeObject( currentNode );


ois = new ObjectInputStream(socObj.getInputStream());


nodeNames = (ArrayList)ois.readObject();


}


}
catch (Exception e)
{
e.printStackTrace();
}


return nodeNames;
}
 

public void selectDestination()
{
Socket socObj;


ObjectOutputStream oos;


ObjectInputStream ois;


String paths = "";





try
{
destination = jComboBox1.getSelectedItem().toString();


System.out.println("The Destination is"+destination);
//String[] me=





if( true)
{


socObj = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(socObj.getOutputStream());


oos.writeObject("getPath");
 

oos.writeObject( nodeName+"#"+destination );


ois = new ObjectInputStream(socObj.getInputStream());


ArrayList<String> allPaths = (ArrayList)ois.readObject(); ArrayList<Integer> energy = new ArrayList<Integer>(); ArrayList<Integer> trust = new ArrayList<Integer>();



ArrayList<String> allNodes = new ArrayList<String>();


ArrayList<String> route = new ArrayList<String>();






for(int i = 0; i < allPaths.size();i++)
{


paths += allPaths.get(i).toString()+"\n";


}





System.out.println("paths"+paths);


clearTable(defaultTableModel);
 



allNodes = (ArrayList)ois.readObject();


System.out.println(allNodes);


for(int j = 0; j < allNodes.size();j=j+6)
{


Vector ds = new Vector();



ds.addElement(allNodes.get(j)); ds.addElement(allNodes.get(j+1)); ds.addElement(allNodes.get(j+2)); ds.addElement(allNodes.get(j+3)); ds.addElement(allNodes.get(j+4)); ds.addElement(allNodes.get(j+5));



System.out.println(ds);


defaultTableModel.addRow(ds);
}
clearTable(rtObj);



route = (ArrayList)ois.readObject();


System.out.println(route);
 

for(int j = 0; j < route.size();j=j+7)
{


Vector ds = new Vector();


ds.addElement(route.get(j)); ds.addElement(route.get(j+1)); ds.addElement(route.get(j+2)); ds.addElement(route.get(j+3)); ds.addElement(route.get(j+4)); ds.addElement(route.get(j+5)); ds.addElement(route.get(j+6)); energy.add(Integer.parseInt(route.get(j+6))); trust.add(Integer.parseInt(route.get(j+4)));

System.out.println(ds);


rtObj.addRow(ds);
}
Object[] bbb={"Direct Trust","Indirect Trust","Function trust"};
String input1 = (String) JOptionPane.showInputDialog(new JFrame(),"Please select path","Title", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("java2sLogo.GIF"), bbb, "Romeo and Juliet");
System.out.println("input"+input1); if(input1.equals("Direct Trust"))
{


System.out.println("before"+energy); Collections.sort(energy);
System.out.println("after"+energy);
 
System.out.println("route"+route);


int he=energy.get(energy.size()-1); System.out.println(he);
String finalpath="";
for(int j = 0; j < route.size();j=j+7)
{


int bb=Integer.parseInt(route.get(j+6)); System.out.println(bb);
if(bb == he)
{
finalpath=route.get(j+2); break;
}


}
System.out.println("Final Path is "+ finalpath);
JOptionPane.showMessageDialog(this,finalpath,"Path is",JOptionPane.INFORMATION_MESSAGE);
randomSelectionProcess(finalpath);
}
if(input1.equals("Indirect Trust"))
{
System.out.println("before trust"+trust); Collections.sort(trust);
System.out.println("after trust"+trust); System.out.println("route"+route);
int he=trust.get(trust.size()-1); System.out.println(he);
String finalpath="";
 
for(int j = 0; j < route.size();j=j+7)
{
int bb=Integer.parseInt(route.get(j+4)); System.out.println(bb);
if(bb == he)
{
finalpath=route.get(j+2); break;
}
}
System.out.println("Final Path is "+ finalpath);
JOptionPane.showMessageDialog(this,finalpath,"Path is",JOptionPane.INFORMATION_MESSAGE);
randomSelectionProcess(finalpath);
}


}



}
catch (Exception e)
{
e.printStackTrace();
}



}


public void randomSelectionProcess(String finalpath)
{
Socket socObj;
 

ObjectInputStream ois;


ObjectOutputStream oos;


String path="";


String fileContent = jTextArea2.getText().trim();



try
{


if( true)
{



socObj = new Socket( ipAddr,2345 );


oos = new ObjectOutputStream( socObj.getOutputStream());


oos.writeObject("getaddress");


oos.writeObject( finalpath );


ois = new ObjectInputStream( socObj.getInputStream());


ArrayList pathInfo = (ArrayList)ois.readObject();


System.out.println("The Paths are"+pathInfo);
 

sendPackets(nodeName,destination,pathInfo,fileContent,finalpath);


}



packets.clear();



}
catch (Exception e)
{
e.printStackTrace();
}


}


public void convertToPackets()
{
//Vector<String> packets = new Vector<String>();


String fileContent = jTextArea2.getText().trim();


if(!(fileContent.equals("")))
{


int packetLength = fileContent.length();


System.out.println("The Packet length is"+packetLength);
 
int noOfPackets = packetLength/48;


int start = 0;


int end = 48;


int position = 0;


System.out.println("The Number of Packet is"+noOfPackets);


if( packetLength > 48 )
{


for(int i = 0;i < noOfPackets;i++)
{


while( packetLength > 0)
{


packetLength -= 48;


String str = position+"@"+fileContent.substring(start,end);


System.out.println("The Packets are"+str);


position++;


start = end;


packets.addElement( str );
 

if( packetLength > 48)
{
end += 48;


}


else
{
end += packetLength;
}
}
}
}


else
{


packets.addElement( fileContent );


}
}
else
{


JOptionPane.showMessageDialog(this,"Please Choose a File");
}
}
 
public void sendPackets(String source,String selectedDest,ArrayList pathInfo,String packetInfo,String randPath)
{


try
{
int y=pathInfo.size();


System.out.println("array size"+y); int alleng=0;



ArrayList p1=pathInfo;





System.out.println("The Paths are1"+p1);


String d1 = (String)p1.get(0);



System.out.println("The Paths are2"+pathInfo);



System.out.println("The Paths are3"+pathInfo);



String nextNode = (String)pathInfo.get(0);


String ipAddress = (String)pathInfo.get(1);
int portNumber = Integer.parseInt((String)pathInfo.get(2));
 



System.out.println("Message from source"+source+"	>"+nextNode);


System.out.println("randpath"+randPath);


Socket socObj = new Socket( ipAddress,portNumber);


ObjectOutputStream oos = new ObjectOutputStream(socObj.getOutputStream());


oos.writeObject("transmit");


oos.writeObject(source);


oos.writeObject( selectedDest );


oos.writeObject( pathInfo );


oos.writeObject( packetInfo );



upDatePathInfo( randPath );










}
catch (Exception e)
 
{
e.printStackTrace();
}


}



public void checksys(String node){


Socket soc;


ObjectOutputStream oos;


ObjectInputStream ois;


try{
if( true ){



System.out.println("check sys node name"+ node); soc = new Socket(ipAddr,2345);

oos = new ObjectOutputStream(soc.getOutputStream());


oos.writeObject("checksys");


oos.writeObject( node );
 
}


}


catch(Exception e){


e.printStackTrace();


}





}
public void upDatePathInfo(String transmitPath){


transmitPath=nodeName+"&"+transmitPath;


Socket soc;


ObjectOutputStream oos;


ObjectInputStream ois;


try{
if( true ){


soc = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(soc.getOutputStream());
 
oos.writeObject("updatepath");


oos.writeObject( transmitPath );



}


}


catch(Exception e){


e.printStackTrace();


}



}



public void upDatePathInfo1(String transmitPath){


transmitPath=nodeName+"&"+transmitPath;


Socket soc;


ObjectOutputStream oos;


ObjectInputStream ois;


try{
 
if( true ){


soc = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(soc.getOutputStream());


oos.writeObject("updatepath1");


oos.writeObject( transmitPath );


ois = new ObjectInputStream(soc.getInputStream());


String reply = (String)ois.readObject();


System.out.println("inserted");


}


}


catch(Exception e){


e.printStackTrace();


}



}


public void getValues(String destName,String path){
 

path=nodeName+"&"+path;


Socket soc;


ObjectOutputStream oos;


ObjectInputStream ois;


Vector val = new Vector();


try{


soc = new Socket(ipAddr,2345);


oos = new ObjectOutputStream(soc.getOutputStream());


oos.writeObject("getdata");


oos.writeObject( destName );


oos.writeObject( path );



ois = new ObjectInputStream(soc.getInputStream());


String pathDetail = (String)ois.readObject();


System.out.println("inserted");
 
//String[] pathInfo = pathDetail.split("@");



String[] pathInfo = pathDetail.split("#");


System.out.println(nodeName);


System.out.println(destName);


System.out.println(pathInfo[0]);


System.out.println(pathInfo[1]);


System.out.println("ms");



//for( int i = 0; i < pathInfo.length; i ++){


Vector v = new Vector();


//StringTokenizer str = new StringTokenizer(pathInfo[i],"#");


//while(str.hasMoreElements()){ v.addElement( nodeName); v.addElement( destName); v.addElement(pathInfo[0]);
//v.addElement( pathInfo[1]); v.addElement( pathInfo[2]+"ms");
//}
SinkNodeName.insertTable( v);
 
//}
}
catch(Exception e){ e.printStackTrace();
}}public static void insertTable(Vector RouteDetail)
{
try
{
System.out.println("calling the table method"); rtObj.addRow( RouteDetail);

}
catch (Exception e)
{
e.printStackTrace();
}}public void clearTable(DefaultTableModel dmodel)
{
try
{
if(dmodel.getRowCount()>0){
for(int i=dmodel.getRowCount()-1;i>-1;--i){ dmodel.removeRow(i);
}
}
}
catch (Exception e)
{
e.printStackTrace();
}
}
 
//============================= Testing
================================//
//=	=//
//= The following main method is just for testing this class you built.=//
//= After testing,you may simply delete it.	=//

//===============================================================
=======//
public static void main(String[] args)
{
/*JFrame.setDefaultLookAndFeelDecorated(true); JDialog.setDefaultLookAndFeelDecorated(true); try
{

UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAn dFeel");
}
catch (Exception ex)
{
System.out.println("Failed loading L&F: "); System.out.println(ex);
}
new SinkNodeName(); */
}
//= End of Testing =
}
