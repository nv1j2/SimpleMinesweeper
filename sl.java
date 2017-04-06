/*java简易扫雷:简单的扫雷 没有胜负判定 没有ctrl+鼠标左键 没有等级*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;  
import java.util.TimerTask;  

public class sl{
    public void textBuK(JButton textBu[],int i,int a[],int leiQun[],int fg[]){//点击“空按钮”的递归 分别传递：按钮，按钮标号，识别过的按钮，按钮对应的，为了方便减少代码的（周围方格）
        if(a[i] == 0 && leiQun[i] == 0){
            a[i] = 1;
            textBu[i].setText("");
            textBu[i].setEnabled(false);//使按钮失效
            for(int m = 0;m < 8;m++){
                int x = (i%10) - (fg[m]%10);
                int y = (i/10) - (fg[m]/10);
                if(x >= 0 && x < 10 && y >= 0 && y < 10 && a[i-fg[m]] == 0){//左上
                    if(leiQun[i-fg[m]] != 0){
                        textBu[i-fg[m]].setText(leiQun[i-fg[m]]+"");
                        textBu[i-fg[m]].setEnabled(false);
                        }
                    textBuK(textBu,i-fg[m],a,leiQun,fg);
                }
            }
        }
    }
    public void slWork(JButton textBu[]){
        int leiQun[];//记录雷 0为什么都没有 9为雷 1~8为周围8格雷个数
        int lei = 10;
        int randomNum;
        leiQun = new int[textBu.length];
        for(int i = 0;i < lei;i++){
            do{
                randomNum = (int)(Math.random()*textBu.length);
            }
            while(leiQun[randomNum] != 0);
            leiQun[randomNum] = 9;
        }
        for(int i = 0,temp,x,y;i < textBu.length;i++){//雷旁边的数字
            if(leiQun[i] == 9)continue;
            temp = 0;
            x = i%10;
            y = i/10;
            if((x - 1) >= 0 && (y - 1) >= 0 && leiQun[i-11] == 9)temp++;
            if((y - 1) >= 0 && leiQun[i-10] == 9)temp++;
            if((x + 1) < 10 && (y - 1) >= 0 && leiQun[i-9] == 9)temp++;
            if((x - 1) >= 0 && leiQun[i-1] == 9)temp++;
            if((x + 1) < 10 && leiQun[i+1] == 9)temp++;
            if((x - 1) >= 0 && (y + 1) < 10 && leiQun[i+9] == 9)temp++;
            if((y + 1) < 10 && leiQun[i+10] == 9)temp++;
            if((x + 1) < 10 && (y + 1) < 10 && leiQun[i+11] == 9)temp++;
            leiQun[i] = temp;
        }
        for(int i = 0;i < textBu.length;i++){
            final int l = i;
            final int fg[] = {11,10,9,1,-1,-9,-10,-11};
            textBu[l].addActionListener(new ActionListener(){    //按钮监听
                public void actionPerformed(ActionEvent e){
                    if(leiQun[l] == 9){
                        textBu[l].setText("雷");
                    }
                    else if(leiQun[l] == 0){
                        int a[];
                        a = new int[textBu.length];//记录识别过的按钮
                        textBuK(textBu,l,a,leiQun,fg);
                    }
                    else{
                        textBu[l].setText(leiQun[l]+"");
                        textBu[l].setEnabled(false);
                    }
                }
            });
            textBu[l].addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){
                    if(e.getButton() == MouseEvent.BUTTON3) {//右键点击
                        if(!SwingUtilities.isRightMouseButton(e))return;
                        textBu[l].setText("¤");
                    }
                }
            });
        }
    }
    public static void main(String args[]){
        //Timer timer = new Timer();
        JFrame frame = new JFrame("扫雷");       //声明窗体 
        String nowTitle = frame.getTitle();  //获得窗口标题名称
        int Fheight = 300, Fwidth=300;//设置长宽
        frame.setSize(Fwidth + 12,Fheight + 34);  //窗体大小,边框宽度和任务栏高度.....
        frame.setResizable(false);  //固定窗体大小,用户不可调整大小
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象 
        Dimension frameSize = frame.getSize();             // 获得窗口大小对象  
        if(frameSize.width > displaySize.width)
            frameSize.width = displaySize.width;           // 窗口的宽度不能大于显示器的宽度  
        if(frameSize.height > displaySize.height)
            frameSize.height = displaySize.height;          // 窗口的高度不能大于显示器的高度  
        frame.setLocation((displaySize.width - frameSize.width)/2,(displaySize.height - frameSize.height)/2); //设置窗口居中显示器显示 
        JButton textBu[];//声明按钮
        Font JBfont = new Font("宋体",Font.BOLD,15); //设置文字类型,粗体,大小
        textBu = new JButton[100];//100个按钮
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));  //布局方式:流布局 中对其,按钮间水平像素0,按钮间垂直像素0
        for(int i = 0;i < textBu.length;i++){
            textBu[i] = new JButton();
            textBu[i].setMargin(new Insets(0,0,0,0));//按钮上文字距离上左下右边框的距离
            textBu[i].setFont(JBfont);
            textBu[i].setPreferredSize(new Dimension(30,30));//按钮长宽
            textBu[i].setFocusPainted(false);//不绘制焦点(虚线框)
            frame.add(textBu[i]);
        }
        Timer timer = new Timer();//计时
        long firstTime = System.currentTimeMillis();//获取从1970年开始至今的毫秒数
        timer.schedule(new TimerTask(){public void run(){
            long lastTime = System.currentTimeMillis();
            String nowTime= String.valueOf((lastTime - firstTime)/1000);
            frame.setTitle(nowTitle+"Time:"+nowTime);
        }},1000,1000);//执行的任务，开始的间隔时间，开始执行后间隔xx再次执行
        new sl().slWork(textBu);
        frame.setVisible(true);//显示窗体可见,放在最后
    }
}
