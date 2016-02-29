package app.musicplayer.model;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.musicplayer.util.Resources;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist {

    private int id;
    private String title;
    private ArrayList<Song> songs;

    public Playlist(int id, String title, ArrayList<Song> songs) {
        this.id = id;
        this.title = title;
        this.songs = songs;
    }

    protected Playlist(int id, String title) {
        this.id = id;
        this.title = title;
        this.songs = null;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public ObservableList<Song> getSongs() {
        return FXCollections.observableArrayList(this.songs);
    }
    
    public void addSong(Song song) {

    	if (!songs.contains(song)) {

    		songs.add(song);

    		try {

    			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    			Document doc = docBuilder.parse(Resources.JAR + "library.xml");

    			XPathFactory xPathfactory = XPathFactory.newInstance();
    			XPath xpath = xPathfactory.newXPath();

    			XPathExpression expr = xpath.compile("/library/playlists/playlist[id/text() = \"" + this.id + "\"]");
    			Node playlist = ((NodeList) expr.evaluate(doc, XPathConstants.NODESET)).item(0);

    			Element songId = doc.createElement("songId");
    			songId.setTextContent(Integer.toString(song.getId()));
    			playlist.appendChild(songId);

    			TransformerFactory transformerFactory = TransformerFactory.newInstance();
    			Transformer transformer = transformerFactory.newTransformer();
    			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    			DOMSource source = new DOMSource(doc);
    			File xmlFile = new File(Resources.JAR + "library.xml");
    			StreamResult result = new StreamResult(xmlFile);
    			transformer.transform(source, result);

    		} catch (Exception ex) {

    			ex.printStackTrace();
    		}
    	}
    }

    @Override
    public String toString() {
        return this.title;
    }
}