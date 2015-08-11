package com.kms.training.codequaility.cleancode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint; // Trainee should know remove non-use packages
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

public class ExportChartApp {
    
    public static void main(String[] args) throws IOException {
	 String inputFile;
	        String outputFile ;
	        String type;
	        
	        inputFile = "";
	        outputFile = "";
	        type = "";
	        
	        /*for(String s : args) {
	            if (s.toLowerCase() == "-h") {
	                System.out.println("usage: ExpChartMain [-h] [-i <arg>] [-o <arg>]");
	                System.out.println("This project is a  training Code Quality (java) meterial");
	                System.out.println(" -h             This project is a  training Code Quality (java) meterial.");
	                System.out.println(" -t=[bar|pie]   Generate report type");
	                System.out.println(" -i=<arg>       Input data file file path (car.txt)");
	                System.out.println(" -o=<arg>       Output generate report file path");
	            } else if (s.toLowerCase().indexOf("-t") == 0 ) {
	                type = s.substring( s.indexOf("=") + 1 );
	            } else if (s.toLowerCase().indexOf("-i") == 0 ) {
	                inputFile = s.substring( s.indexOf("=") + 1 );
	            } else if (s.toLowerCase().indexOf("-o") == 0 ) {
	                outputFile = s.substring( s.indexOf("=") + 1 );
	            }
	        }*/
	        for(String s : args) {
	            if (s.toLowerCase() == "-h") {
	                System.out.println("usage: ExpChartMain [-h] [-i <arg>] [-o <arg>]");
	                System.out.println("This project is a  training Code Quality (java) meterial");
	                System.out.println(" -h             This project is a  training Code Quality (java) meterial.");
	                System.out.println(" -t=[bar|pie]   Generate report type");
	                System.out.println(" -i=<arg>       Input data file file path (car.txt)");
	                System.out.println(" -o=<arg>       Output generate report file path");
	            } else if (s.toLowerCase().indexOf("-t") == 0 ) {
	                type = s.substring( s.indexOf("=") + 1 );
	            } else if (s.toLowerCase().indexOf("-i") == 0 ) {
	                inputFile = s.substring( s.indexOf("=") + 1 );
	            } else if (s.toLowerCase().indexOf("-o") == 0 ) {
	                outputFile = s.substring( s.indexOf("=") + 1 );
	            }
	        }
	        
	        
	        List<String[]> repo = new ArrayList<String[]>();
	       
	        // Open the file
	        FileInputStream fstream = new FileInputStream(inputFile);
	        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	        String strLine;
	        String[] headers = null;
	        // Read File Line By Line
	        while ((strLine = br.readLine()) != null) {
	            // Split 'tab' delimiter
	            String[] tabs = strLine.split("\\t");
	            if (headers == null) {
	                headers = tabs;
	            }
	            else {
	                repo.add(tabs);
	            }
	        }
	        // Close the input stream
	        br.close();

	        // Header CarNo \t Manufature \t Speed \t Millage \t User Rating
	        // Calculate manufacture's average speed
	        Map<String, Integer> manufactureCount = new TreeMap<String, Integer>();
	        for (String[] item : repo) {
	            String manufacture = item[1];
	            int count = 1;
	            if (manufactureCount.containsKey(manufacture)) {
	                count += manufactureCount.get(manufacture);
	            }
	            manufactureCount.put(manufacture, count);
	        }

	        Map<String, Double> speedSum = new TreeMap<String, Double>();
	        for (String[] item : repo) {
	            String manufacture = item[1];
	            double speed = Double.parseDouble(item[2]);
	            if (manufactureCount.containsKey(manufacture)) {
	                speed += manufactureCount.get(manufacture);
	            }
	            speedSum.put(manufacture, speed);
	        }

	        Map<String, Double> speedAverage = new TreeMap<String, Double>();
	        double max = 0.0;
	        for (Entry<String, Integer> entry : manufactureCount.entrySet()) {
	            double avgSpeed = 0.0;
	            // divide zero
	            if (speedSum.containsKey(entry.getKey()) && entry.getValue() != 0) {
	                avgSpeed = speedSum.get(entry.getKey()) / entry.getValue();
	            }
	            if (max < avgSpeed) {
	                max = avgSpeed;
	            }
	            speedAverage.put(entry.getKey(), avgSpeed);
	        }
	        
	        if (type.toLowerCase().equals("bar")) {
	            
	            int width = 1024; /* Width of the image */
	            int height = 768; /* Height of the image */
	            
	            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            Graphics2D g2 = image.createGraphics();       
	            Shape chartArea = new Rectangle2D.Double(0, 0, width, height);
	            g2.clip(chartArea);
	            g2.fill(chartArea);
	            
	            g2.setPaint(Color.black);
	            g2.setFont(new Font("Dialog", Font.BOLD, 18));
	            // draw the title and subtitles...
	            g2.drawString("Car Speed Statistics", width /2 , 20);
	            Random rand = new Random();
	            
	            int scalingBar = width / (speedAverage.size() + 1);
	            int chartHeight = (height - 20 - 100);
	            double heightUnit = chartHeight / max;
	            int n = 0;
	            int x = 0;
	            for (Entry<String, Double> speedEntry : speedAverage.entrySet()) {
	                float r = rand.nextFloat();
	                float g = rand.nextFloat();
	                float b = rand.nextFloat();
	                Color randomColor = new Color(r, g, b);
	                g2.setPaint(randomColor);
	                int columnHeight = ((Double) (speedEntry.getValue() * heightUnit)).intValue();
	                x = (n++ == 0 ? 60 : x + scalingBar + 20);
	                int y = (chartHeight - columnHeight + 50);
	                g2.fill(new Rectangle2D.Double(x, y, 30, columnHeight));
	                // Logic round number ......
	                double value = (double) Math.round(speedEntry.getValue() * 100) / 100;
	                g2.drawString(speedEntry.getKey() + "(" +  value + ")", x - 10, chartHeight + 80);
	            }        
	            g2.dispose();
	            
	            File lineChartFile = new File(outputFile);
	            OutputStream out = new BufferedOutputStream(new FileOutputStream(lineChartFile));
	            ImageIO.write(image, "png", out);
	            
	            System.out.println("Bar Chart Export done! ");
	        } else  {
	            int width = 1024; /* Width of the image */
	            int height = 768; /* Height of the image */
	            BufferedImage image3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            Graphics2D g3 = image3.createGraphics();     
	            Shape chartArea = new Rectangle2D.Double(0, 0, width, height);
	            g3.clip(chartArea);
	            g3.fill(chartArea);

	            g3.setPaint(Color.black);
	            g3.setFont(new Font("Dialog", Font.BOLD, 18));
	            // draw the title and subtitles...
	            g3.drawString("Car Speed Statistics", width /2 , 20);
	            Random rand3 = new Random();
	            // Should use collection util to get max
	            double ts = 0;
	            for(double s : speedAverage.values() ) {
	                ts += s;
	            }
	            double curValue = 0.0D;
	            int startAngle = 0;
	            int pieX , pieY, pieWidth, pieHeight, legendX, legendY;
	            pieX = 100;
	            pieY = 50;
	            pieWidth = 400;
	            pieHeight = 400;
	            legendX = 600;
	            legendY = 100;
	           
	            for(Entry<String, Double> s : speedAverage.entrySet()) {
	                startAngle = (int) (curValue * 360 / ts);
	                int arcAngle = (int) (s.getValue() * 360 / ts);
	                float r = rand3.nextFloat();
	                float g = rand3.nextFloat();
	                float b = rand3.nextFloat();
	                Color randomColor = new Color(r, g, b);
	                g3.setPaint(randomColor);
	                g3.fillArc(pieX, pieY, pieWidth, pieHeight, startAngle, arcAngle);
	                curValue += s.getValue();
	                g3.setFont(new Font("Dialog", Font.BOLD, 18));
	                double value = (double) Math.round(s.getValue() * 100) / 100;
	                g3.drawString(s.getKey() + "(" +  value + ")", legendX, legendY);
	                legendY += 30;
	            }
	            g3.dispose();
	            File pieChartFile = new File("piechart.png");
	            OutputStream out3 = new BufferedOutputStream(new FileOutputStream(pieChartFile));
	            ImageIO.write(image3, "png", out3);
	            System.out.println("Pie Chart Export done! ");
	        }
    }
}
