// BV Ue2 WS20/21 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ws20;

import java.io.File;
import bv_ws20.DPCM.FilterType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class AppController {

    private static final String initialFileName = "test1.jpg";
    private static File fileOpenPath = new File(".");
    private static final DPCM dpcm = new DPCM();

    @FXML
    private ImageView originalImage;

    @FXML
    private ImageView predictionErrorImage;

    @FXML
    private ImageView reconstructedImage;

    @FXML
    private Label entropyOriginalImg;

    @FXML
    private Label entropyPredictionImg;

    @FXML
    private ComboBox<FilterType> predictionBox;

    @FXML
    private Slider quantizationSlider;

    @FXML
    private Label quantizationLabel;

    @FXML
    private Label entropyReImg;

    //@FXML
    //private Label MSE;

    @FXML
    private Label messageLabel;

    @FXML
    void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(fileOpenPath);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            fileOpenPath = selectedFile.getParentFile();
            RasterImage img = new RasterImage(selectedFile);
            img.convertToGray();
            img.setToView(originalImage);
            processImages();
            messageLabel.getScene().getWindow().sizeToScene();
        }
    }

    @FXML
    public void quantizationSliderChanged() {
        quantizationLabel.setText(String.format("%.2f", quantizationSlider.getValue()));
        processImages();
    }

    @FXML
    public void predictorChanged () {
        processImages();
    }

    @FXML
    public void initialize() {
        // set combo boxes items
        predictionBox.getItems().addAll(FilterType.values());
        predictionBox.setValue(FilterType.A);

        // initialize parameters

        // load and process default image
        RasterImage img = new RasterImage(new File(initialFileName));
        img.convertToGray();
        img.setToView(originalImage);
        processImages();
    }
    private void processImages() {
        if(originalImage.getImage() == null) 
        	return; // no image: nothing to do

        long startTime = System.currentTimeMillis();

        RasterImage originalImg = new RasterImage(originalImage);
        
        RasterImage predErrorImg = new RasterImage(originalImg.width, originalImg.height);
        RasterImage reconstructImg = new RasterImage(originalImg.width, originalImg.height);
        
        
        
        if ( predictionBox.getValue() == FilterType.A )  dpcm.horizontal(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
        else if ( predictionBox.getValue() == FilterType.B )  dpcm.vertical(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
        else if ( predictionBox.getValue() == FilterType.C )  dpcm.diagonal(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
        else if ( predictionBox.getValue() == FilterType.ABC )  dpcm.diagonal(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
        else if ( predictionBox.getValue() == FilterType.ABC )  dpcm.diagonal(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
        
        
        
//        switch (predictionBox.getValue()) {
//            case A:
//                dpcm.horizontal(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
//                break;
//            case B:
//                dpcm.vertical(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
//                break;
//            case C:
//                dpcm.diagonal(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
//                break;
//            case ABC:
//                dpcm.abc(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
//                break;
//            case adaptiv:
//                dpcm.adaptiv(originalImg, predErrorImg,(float) quantizationSlider.getValue(), reconstructImg);
//                break;
//        }

        predErrorImg.setToView(predictionErrorImage);
        reconstructImg.setToView(reconstructedImage);

        entropyOriginalImg.setText(String.format("%.2f", dpcm.entropy(originalImg)));
        entropyPredictionImg.setText(String.format("%.2f", dpcm.entropy(predErrorImg)));
        entropyReImg.setText(String.format("%.2f", dpcm.entropy(reconstructImg)));
        
        //MSE.setText("" + dpcm.getMSE());
        messageLabel.setText("Processing time: " + (System.currentTimeMillis() - startTime) + " ms");
        

    }
}
