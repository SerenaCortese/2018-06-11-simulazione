/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.ufo.model.AnnoAvvistamenti;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {
	
	private Model model;
	private AnnoAvvistamenti aa;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<AnnoAvvistamenti> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleAnalizza(ActionEvent event) {
    	txtResult.clear();
    	if(aa == null || boxStato.getValue() == null) {
    		txtResult.setText("Scegliere un anno tra quelli presenti nel menu e premere il tasto Avvistamenti");
    		return;
    	}
    	String stato = boxStato.getValue();
    	
    	List<String> statiPrecedenti = model.getPrecedentiDi(stato);
    	txtResult.appendText("Stati PRECEDENTI di \""+ stato + "\" : \n");
    	txtResult.appendText(statiPrecedenti.toString()+"\n");
    	
    	List<String> statiSuccessivi = model.getSuccessiviDi(stato);
    	txtResult.appendText("Stati SUCCESSIVI di "+ stato + " : \n");
    	txtResult.appendText(statiSuccessivi.toString()+"\n");
    	
    	Set<String> statiRaggiungibili = model.getRaggiungiliDa(stato);
    	if(statiRaggiungibili == null ) {
    		txtResult.appendText("Non vi sono stati raggiungibili da \""+ stato+ "\".");
    		return;
    	}
    	txtResult.appendText("Gli Stati RAGGIUNGIBILI da \" "+ stato + " \" sono "+ statiRaggiungibili.size()+" : \n");
    	txtResult.appendText(statiRaggiungibili.toString()+"\n");
    	
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	boxStato.getItems().clear();
    	aa = boxAnno.getValue();
    	if (aa == null) {
    		txtResult.setText("Scegliere un anno tra quelli presenti nel menu");
    		return;
    	}
    	Year anno = aa.getYear();
    	model.createGraph(anno);
    	List<String> stati = model.getStates();
    	if( stati == null) {
    		txtResult.setText("Non ci sono stati con almeno un avvistamento nell'anno selezionato.\nCambiare anno.");
    		return;
    	}
    	boxStato.getItems().addAll(stati);
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	if(aa == null || boxStato.getValue() == null) {
    		txtResult.setText("Scegliere un anno tra quelli presenti nel menu e premere il tasto Avvistamenti");
    		return;
    	}
    	String stato = boxStato.getValue();
    	List<String> sequenza = model.getCamminoPiuLungo(stato);
    	txtResult.appendText("Cammino più lungo: \n");
    	if(sequenza == null) {
    		txtResult.appendText("Errore nella ricerca della sequenza.\n");
    		return;
    	}
    	txtResult.appendText(sequenza.toString()+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxAnno.getItems().addAll(model.getYears());
		
	}
}
