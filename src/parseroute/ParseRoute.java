/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parseroute;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author yanhaohu
 */
public class ParseRoute {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, MalformedURLException, ParserConfigurationException, SAXException {
        getRoute("uiowa","red");
        getStop("uiowa","red");
        // TODO code application logic here
    }
    private static void getRoute(String agent,String route){
        try (PrintWriter writer = new PrintWriter ("/Users/yanhaohu/Desktop/"+route+".txt")) {
            URL stopsURL = new URL ("http://api.ebongo.org/route?agency="+agent+"&route="+route+"&api_key=xApBvduHbU8SRYvc74hJa7jO70Xx4XNO");
            InputStream xml = stopsURL.openStream();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xml);
            NodeList nList = doc.getElementsByTagName("path");
            NodeList MinMaxList = doc.getElementsByTagName("route");
            Node MMNode = MinMaxList.item(0);
            Element mElement = (Element) MMNode;
            writer.println(mElement.getAttribute("min_lat")+","+mElement.getAttribute("min_lng")+","+mElement.getAttribute("max_lat")+","+mElement.getAttribute("max_lng"));
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList PointList = nNode.getChildNodes();
                    for (int i = 0; i < PointList.getLength(); i++) {
                        Node PointNode = PointList.item(i);
                        if (PointNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element pElement = (Element) PointNode;
                            String pLat = pElement.getAttribute("lat");
                            String pLng = pElement.getAttribute("lng");
                            writer.println(pLat+","+pLng);
                            System.out.print("lol");
                        }
                    }
                    writer.println(";");
                    System.out.println("Endln");
                    
                }
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseRoute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ParseRoute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParseRoute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException ex) {
            Logger.getLogger(ParseRoute.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static void getStop(String agent,String route) throws MalformedURLException, IOException, ParserConfigurationException, SAXException{
        try (PrintWriter writer = new PrintWriter ("/Users/yanhaohu/Desktop/stop/"+route+".txt")) {
            URL stopsURL = new URL ("http://api.ebongo.org/route?agency="+agent+"&route="+route+"&api_key=xApBvduHbU8SRYvc74hJa7jO70Xx4XNO");
            InputStream xml = stopsURL.openStream();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xml);
            NodeList nList = doc.getElementsByTagName("stop");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element eElement = (Element) nNode;
                    String number = eElement.getAttribute("number");
                    String title = eElement.getAttribute("title");
                    String lat = eElement.getAttribute("lat");
                    String lng = eElement.getAttribute("lng");
                    System.out.println("Stop number: " + eElement.getAttribute("number"));
                    writer.println(number+","+title+","+lat+","+lng);
                }
            }
        }
    }
    
}
