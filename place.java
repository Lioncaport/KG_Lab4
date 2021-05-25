package workspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

public class place extends JPanel
{
    private int N;
    private double[][] Poligon;
    private int nPoligon;
    private int n;
    private double[][] Segment;
    private int nSegment;
    private Point mousPt;

    private double[][] t;
    private int tCount;
    private double[][] tResult;
    private int tRCount;

    boolean cross(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4 ,double dot[])
    {
        //считаем уравнения прямых проходящих через отрезки
        double a1 = -(y2-y1);
        double b1 = x2-x1;
        double d1 = -(a1*x1 + b1*y1);

        double a2 = -(y4-y3);
        double b2 = x4-x3;
        double d2 = -(a2*x3 + b2*y3);

        //подставляем концы отрезков, для выяснения в каких полуплоскотях они
        double seg1_line2_start = a2*x1 + b2*y1 + d2;
        double seg1_line2_end = a2*x2 + b2*y2 + d2;

        double seg2_line1_start = a1*x3 + b1*y3 + d1;
        double seg2_line1_end = a1*x4 + b1*y4 + d1;

        //если концы одного отрезка имеют один знак, значит он в одной полуплоскости и пересечения нет.
        if (seg1_line2_start * seg1_line2_end >= 0 || seg2_line1_start * seg2_line1_end >= 0)
            return false;

        double u = seg1_line2_start / (seg1_line2_start - seg1_line2_end);
        dot[0] = x1 + (x2 - x1) * u;
        dot[1] = y1 + (y2 - y1) * u;
        return true;
    }

    public place()
    {
        setPreferredSize(new Dimension(1000,600));

        Poligon = new double[][]{{250,450},{350,200},{400,350},{450,150},{500,380},{550,170},{600,300},{680,320},{750,450}};
        N = Poligon.length;
        nPoligon = -1;
        Segment = new double[][]{{100,330},{900,330}};
        n = Segment.length;
        nSegment = -1;
        t = new double[N][2];
        tResult = new double[N+2][2];

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                mousPt = e.getPoint();
                for (int i = 0; i < n; i++)
                {
                    if (Math.pow(Segment[i][0]-mousPt.x,2) + Math.pow(Segment[i][1]-mousPt.y,2) <= 25)
                    {
                        nSegment = i;
                        i = n;
                    }
                    else nSegment = -1;
                }

                if (nSegment == -1)
                {
                    for (int i = 0; i < N; i++)
                    {
                        if (Math.pow(Poligon[i][0]-mousPt.x,2) + Math.pow(Poligon[i][1]-mousPt.y,2) <= 25)
                        {
                            nPoligon = i;
                            i = N;
                        }
                        else nPoligon = -1;
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (nSegment != -1)
                {
                    Segment[nSegment][0] = e.getX();
                    Segment[nSegment][1] = e.getY();
                    repaint();
                }
                else if (nPoligon != -1)
                {
                    Poligon[nPoligon][0] = e.getX();
                    Poligon[nPoligon][1] = e.getY();
                    repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g)
    {
        int width = 1000;
        int hight = 600;
        super.paintComponent(g);

        // отрисовка вершин для захвата мышкой
        g.setColor(Color.MAGENTA);
        for (int i = 0; i < N; i++)
        {
            g.fillOval((int) Poligon[i][0] - 5, (int) Poligon[i][1] - 5, 10, 10);
        }
        for (int i = 0; i < n; i++)
        {
            g.fillOval((int) Segment[i][0] - 5, (int) Segment[i][1] - 5, 10, 10);
        }

        // отрисовка произвольного многоугольника
        g.setColor(Color.blue);
        for (int i = 0; i < N-1; i++)
        {
            g.drawLine((int)Poligon[i][0],(int)Poligon[i][1],(int)Poligon[i+1][0],(int)Poligon[i+1][1]);
        }
        g.drawLine((int)Poligon[0][0],(int)Poligon[0][1],(int)Poligon[N-1][0],(int)Poligon[N-1][1]);

        // отрисовка отрезка
        g.setColor(Color.green);
        g.drawLine((int)Segment[0][0],(int)Segment[0][1],(int)Segment[1][0],(int)Segment[1][1]);

        // определение точек пересечения отрезков
        tCount = 0;
        double[] dot = new double[2];
        for (int i = 0; i < N-1; i++)
        {
            if (cross(Segment[0][0],Segment[0][1],Segment[1][0],Segment[1][1],Poligon[i][0],Poligon[i][1],Poligon[i+1][0],Poligon[i+1][1],dot))
            {
                t[tCount][0] = dot[0];
                t[tCount][1] = dot[1];
                tCount += 1;
            }
        }
        if (cross(Segment[0][0],Segment[0][1],Segment[1][0],Segment[1][1],Poligon[0][0],Poligon[0][1],Poligon[N-1][0],Poligon[N-1][1],dot))
        {
            t[tCount][0] = dot[0];
            t[tCount][1] = dot[1];
            tCount += 1;
        }

        // отрисовка точек пересечения отрезков
        g.setColor(Color.red);
        for (int i = 0; i < tCount; i++)
        {
            g.fillOval((int) t[i][0] - 5, (int) t[i][1] - 5, 10, 10);
        }

        // лучевая проверка положения точки P1 относительно окна
        int RayCount = 0;
        double[] RayDot = new double[2];
        for (int i = 0; i < N-1; i++)
        {
            if (cross(Segment[0][0],Segment[0][1],Segment[0][0],-1,Poligon[i][0],Poligon[i][1],Poligon[i+1][0],Poligon[i+1][1],RayDot))
            {
                RayCount += 1;
            }
        }
        if (cross(Segment[0][0],Segment[0][1],Segment[0][0],-1,Poligon[0][0],Poligon[0][1],Poligon[N-1][0],Poligon[N-1][1],RayDot))
        {
            RayCount += 1;
        }

        // отрезки, образованные выделенными точками, лежат в окне
        tRCount = 0;
        if (RayCount % 2 == 1)
        {
            // P1 лежит в окне
            tResult[0][0] = Segment[0][0];
            tResult[0][1] = Segment[0][1];
            tRCount += 1;
            for (int i = 0; i < tCount ; i++)
            {
                tResult[i+1][0] = t[i][0];
                tResult[i+1][1] = t[i][1];
            }
            tRCount += tCount;
            if (tCount % 2 == 0)
            {
                // количество пересечений чётно
                tResult[tCount+1][0] = Segment[1][0];
                tResult[tCount+1][1] = Segment[1][1];
                tRCount += 1;
            }
        }
        else
        {
            // P1 лежит вне окна
            for (int i = 0; i < tCount ; i++)
            {
                tResult[i][0] = t[i][0];
                tResult[i][1] = t[i][1];
            }
            tRCount += tCount;
            if (tCount % 2 == 1)
            {
                // количество пересечений нечетно
                tResult[tCount][0] = Segment[1][0];
                tResult[tCount][1] = Segment[1][1];
                tRCount += 1;
            }
        }

        // отрисовка видимых частей отрезков
        g.setColor(Color.red);
        for (int i = 0; i < tRCount; i += 2)
        {
            g.drawLine((int) tResult[i][0], (int) tResult[i][1], (int) tResult[i+1][0], (int) tResult[i+1][1]);
        }
    }
}