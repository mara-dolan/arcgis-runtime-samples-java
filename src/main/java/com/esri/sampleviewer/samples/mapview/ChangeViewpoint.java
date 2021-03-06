/*
 * Copyright 2015 Esri. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.esri.sampleviewer.samples.mapview;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Map;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.Viewpoint;

/**
 * This sample demonstrates three different ways in which one can change the
 * Viewpoint (visible area) of a Map.
 * <h4>How it Works</h4>
 * 
 * <li>The animation duration of a {@link Viewpoint} can be set by using the
 * {@link MapView#setViewpointWithDurationAsync} method.</li>
 * <li>You can use the {@link MapView#setViewpointCenterWithScaleAsync} method
 * to center the Viewpoint around a Point and give it a scale.</li>
 * <li>Lastly, the {@link MapView#setViewpointGeometryAsync} method allows the
 * Viewpoint to be centered around the Geometry that is passed.</li>
 */
public class ChangeViewpoint extends Application {

  private MapView mapView;
  private SpatialReference spatialReference;

  private static final int SCALE = 5000;

  @Override
  public void start(Stage stage) throws Exception {

    // create stack pane and application scene
    StackPane stackPane = new StackPane();
    Scene scene = new Scene(stackPane);
    scene.getStylesheets().add(getClass().getResource(
        "../resources/SamplesTheme.css").toExternalForm());

    // set title, size, and add scene to stage
    stage.setTitle("Change Viewpoint Sample");
    stage.setWidth(800);
    stage.setHeight(700);
    stage.setScene(scene);
    stage.show();

    // create a control panel
    VBox vBoxControl = new VBox(6);
    vBoxControl.setMaxSize(260, 240);
    vBoxControl.getStyleClass().add("panel-region");

    // create sample description
    Label descriptionLabel = new Label("Sample Description:");
    descriptionLabel.getStyleClass().add("panel-label");
    TextArea description = new TextArea("This sample shows different ways " +
        "to change the Viewpoint of a Map.");
    description.setWrapText(true);
    description.autosize();
    description.setEditable(false);

    // create buttons for interaction
    Button animateButton = new Button("LONDON (Animate)");
    Button centerButton = new Button("WATERLOO (Center and Scaled)");
    Button geometryButton = new Button("WESTMINSTER (Geometry)");
    animateButton.setMaxWidth(Double.MAX_VALUE);
    centerButton.setMaxWidth(Double.MAX_VALUE);
    geometryButton.setMaxWidth(Double.MAX_VALUE);

    animateButton.setOnAction(e -> {
      // create the London location point
      Point londonPoint = new Point(-14093, 6711377, spatialReference);
      // create the viewpoint with the London point and scale
      Viewpoint viewpoint = new Viewpoint(londonPoint, SCALE);
      // set the map views's viewpoint to London with a seven second duration
      mapView.setViewpointWithDurationAsync(viewpoint, 7);
    });

    centerButton.setOnAction(e -> {
      // create the Waterloo location point
      Point waterlooPoint = new Point(-12153, 6710527, spatialReference);
      // set the map views's viewpoint centered on Waterloo and scaled
      mapView.setViewpointCenterWithScaleAsync(waterlooPoint, SCALE);
    });

    geometryButton.setOnAction(e -> {
      // create a collection of points around Westminster
      PointCollection westminsterPoints = new PointCollection(spatialReference);
      westminsterPoints.add(new Point(-13823, 6710390));
      westminsterPoints.add(new Point(-13823, 6710150));
      westminsterPoints.add(new Point(-14680, 6710390));
      westminsterPoints.add(new Point(-14680, 6710150));

      Polyline geometry = new Polyline(westminsterPoints);

      // set the map views's viewpoint to Westminster
      mapView.setViewpointGeometryAsync(geometry);
    });

    // add controls to the user interface panel
    vBoxControl.getChildren().addAll(descriptionLabel, description,
        animateButton, centerButton, geometryButton);
    try {

      // create map with imagery basemap
      Map map = new Map(Basemap.createImageryWithLabels());

      // create a view and set map to it
      mapView = new MapView();
      mapView.setMap(map);

      // create spatial reference for all points
      spatialReference = SpatialReferences.getWebMercator();
      // create point for starting location
      Point startPoint = new Point(-14093, 6711377, spatialReference);

      // set viewpoint of map view to starting point and scaled
      mapView.setViewpointCenterWithScaleAsync(startPoint, SCALE);

      // add map view and control panel to stack pane
      stackPane.getChildren().addAll(mapView, vBoxControl);
      StackPane.setAlignment(vBoxControl, Pos.TOP_LEFT);
      StackPane.setMargin(vBoxControl, new Insets(10, 0, 0, 10));
    } catch (Exception e) {
      // on any error, print the stack trace
      e.printStackTrace();
    }
  }

  /**
   * Stops and releases all resources used in application.
   * 
   * @throws Exception if security manager doesn't allow JVM to exit with
   *           current status
   */
  @Override
  public void stop() throws Exception {

    // release resources when the application closes
    if (mapView != null) {
      mapView.dispose();
    }
    Platform.exit();
    System.exit(0);
  }

  /**
   * Opens and runs application.
   * 
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }

}
