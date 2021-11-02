
package appagenda;

import entidades.Persona;
import entidades.Provincia;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;


public class InsertarPersonaViewController implements Initializable 
{
    private Pane rootAgendaView;
    private TableView tableViewPrevio;
    private Persona persona;
    private EntityManager entityManager;
    private boolean nuevaPersona;
    
    public static final char CASADO = 'C';
    public static final char SOLTERO = 'S';
    public static final char VIUDO = 'V';
    
    public static final String CARPETA_FOTOS = "src/appagenda/Fotos";

    @FXML
    private TextField fieldInsertarPersonaNombre;
    @FXML
    private TextField fieldInsertarPersonaApellidos;
    @FXML
    private TextField fieldInsertarPersonaTlfno;
    @FXML
    private TextField fieldInsertarPersonaEmail;
    @FXML
    private TextField fieldInsertarPersonaHijos;
    @FXML
    private TextField fieldInsertarPersonaSalario;
    @FXML
    private DatePicker calendarioInsertarPersona;
    @FXML
    private RadioButton rbuttonSoltero;
    @FXML
    private RadioButton rbuttonCasado;
    @FXML
    private RadioButton rbuttonViudo;
    @FXML
    private CheckBox checkInsertarPersonaJubilado;
    @FXML
    private Button btnInsertarPersonaGuardar;
    @FXML
    private Button btnInsertarPersonaCancelar;
    @FXML
    private ComboBox<Provincia> desplegableInsertarPersonaProvincia;
    @FXML
    private Button btnInsertarPersonaExaminar;
    @FXML
    private AnchorPane rootInsertarPersonaView;
    @FXML
    private ToggleGroup estadoCivil;
    @FXML
    private GridPane gridInsercionPersona;
    @FXML
    private Label labelInsertarPersonaNombre;
    @FXML
    private Label labelInsertarPersonaApellidos;
    @FXML
    private Label labelInsertarPersonaTlfno;
    @FXML
    private Label labelInsertarPersonaEmail;
    @FXML
    private Label labelInsertarPersonaFNac;
    @FXML
    private Label labelInsertarPersonaNHijos;
    @FXML
    private Label labelInsertarPersonaECivil;
    @FXML
    private Label labelInsertarPersonaSalario;
    @FXML
    private Label labelInsertarPersonaJubilacion;
    @FXML
    private Label labelInsertarPersonaProvincia;
    @FXML
    private Label labelInsertarPersonaFoto;
    @FXML
    private HBox hboxInsertarPersonaCivil;
    @FXML
    private ImageView panelFoto;
    @FXML
    private Button btnSuprimirFoto;
  
    
    
    public void setTableViewPrevio(TableView tableViewPrevio)
    {
        this.tableViewPrevio=tableViewPrevio;
    }

    public void setRootAgendaView(Pane rootAgendaView)
    {
        this.rootAgendaView = rootAgendaView;
    }

    public void setPersona(EntityManager entityManager, Persona persona, Boolean nuevaPersona)
    {
        this.entityManager = entityManager;
        entityManager.getTransaction().begin();
        if (!nuevaPersona)
        {
            this.persona=entityManager.find(Persona.class,persona.getId());
        } 
        else 
        {
            this.persona=persona;
        }
        this.nuevaPersona=nuevaPersona;
    }
    
    
    public void mostrarDatos()
    {
        fieldInsertarPersonaNombre.setText(persona.getNombre());
        fieldInsertarPersonaApellidos.setText(persona.getApellidos());
        fieldInsertarPersonaTlfno.setText(persona.getTelefono());
        fieldInsertarPersonaEmail.setText(persona.getEmail());
        if (persona.getNumHijos() != null)
        {
            fieldInsertarPersonaHijos.setText(persona.getNumHijos().toString());
        }
        if (persona.getSalario() != null)
        {
            fieldInsertarPersonaSalario.setText(persona.getSalario().toString());
        }
        if (persona.getJubilado() != null)
        {
            checkInsertarPersonaJubilado.setSelected(persona.getJubilado());
        }
        if (persona.getEstadoCivil() != null)
        {
            switch(persona.getEstadoCivil())
            {
                case CASADO:
                    rbuttonCasado.setSelected(true);
                    break;
                case SOLTERO:
                    rbuttonSoltero.setSelected(true);
                    break;
                case VIUDO:
                    rbuttonViudo.setSelected(true);
                    break;
            }
        }
        if (persona.getFechaNacimiento() != null)
        {
            Date date=persona.getFechaNacimiento();
            Instant instant=date.toInstant();
            ZonedDateTime zdt=instant.atZone(ZoneId.systemDefault());
            LocalDate localDate=zdt.toLocalDate();
            calendarioInsertarPersona.setValue(localDate);
        }

        Query queryProvinciaFindAll = entityManager.createNamedQuery("Provincia.findAll");
        List listProvincia = queryProvinciaFindAll.getResultList();
        desplegableInsertarPersonaProvincia.setItems(FXCollections.observableList(listProvincia));
        if (persona.getProvincia() != null)
        {
            desplegableInsertarPersonaProvincia.setValue(persona.getProvincia());
        }
        desplegableInsertarPersonaProvincia.setCellFactory((ListView<Provincia> l) -> new ListCell<Provincia>() 
        {
            @Override
            protected void updateItem(Provincia provincia, boolean empty)
            {
                System.out.println("Hasta aqui 1");
                super.updateItem(provincia, empty);
                if (provincia == null || empty)
                {
                    setText("");
                } 
                else 
                {
                    setText(provincia.getCodigo() + "-" + provincia.getNombre());
                }
            }
        });
        desplegableInsertarPersonaProvincia.setConverter(new StringConverter<Provincia>()
        {
            @Override
            public String toString(Provincia provincia)
            {
                if (provincia == null)
                {
                    return null;
                } 
                else 
                {
                    return provincia.getCodigo() + "-" + provincia.getNombre();
                }
            }
            @Override
            public Provincia fromString(String userId)
            {
                return null;
            }
        });
        
        if (persona.getFoto() != null)
        {
            String imageFileName = persona.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists())
            {
                Image image = new Image(file.toURI().toString());
                panelFoto.setImage(image);
            } 
            else 
            {
                Alert alert = new Alert(AlertType.INFORMATION, "No se encuentra la imagen en " + file.toURI().toString());
                alert.showAndWait();
            }
        }
        
    }
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }    

    @FXML
    private void onActionButtonGuardar(ActionEvent event) 
    {
        boolean errorFormato = false;

        persona.setNombre(fieldInsertarPersonaNombre.getText());
        persona.setApellidos(fieldInsertarPersonaApellidos.getText());
        persona.setTelefono(fieldInsertarPersonaTlfno.getText());
        persona.setEmail(fieldInsertarPersonaEmail.getText());
        
        if (!fieldInsertarPersonaHijos.getText().isEmpty())
        {
            try 
            {
                persona.setNumHijos(Short.valueOf(fieldInsertarPersonaHijos.getText()));
            } 
            catch(NumberFormatException ex)
            {
                errorFormato = true;
                Alert alert = new Alert(AlertType.INFORMATION, "Número de hijos no válido");
                alert.showAndWait();
                fieldInsertarPersonaHijos.requestFocus();
            }
        }
   
        if (!fieldInsertarPersonaSalario.getText().isEmpty())
        {
            try 
            {
                persona.setSalario(BigDecimal.valueOf(Double.valueOf(fieldInsertarPersonaSalario.getText()).doubleValue()));
            }  
            catch(NumberFormatException ex) 
            {
                errorFormato = true;
                Alert alert = new Alert(AlertType.INFORMATION, "Salario no válido");
                alert.showAndWait();
                fieldInsertarPersonaSalario.requestFocus();
            }
        }

        persona.setJubilado(checkInsertarPersonaJubilado.isSelected());

        if (rbuttonCasado.isSelected())
        {
            persona.setEstadoCivil(CASADO);
        } 
        else if (rbuttonSoltero.isSelected())
        {
            persona.setEstadoCivil(SOLTERO);
        } 
        else if (rbuttonViudo.isSelected())
        {
            persona.setEstadoCivil(VIUDO);
        }

        if (calendarioInsertarPersona.getValue() != null)
        {
            LocalDate localDate = calendarioInsertarPersona.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            persona.setFechaNacimiento(date);
        } 
        else 
        {
            persona.setFechaNacimiento(null);
        }
        
        if (desplegableInsertarPersonaProvincia.getValue() != null)
        {
            persona.setProvincia(desplegableInsertarPersonaProvincia.getValue());
        } 
        else 
        {
            Alert alert = new Alert(AlertType.INFORMATION,"Debe indicar una provincia");
            alert.showAndWait();
            errorFormato = true;
        }
        
        
        // Recoger datos de pantalla
        if (!errorFormato) 
        { 
            // Los datos introducidos son correctos
            try 
            {
                if (nuevaPersona)
                {
                    entityManager.persist(persona);
                } 
                else 
                {
                    entityManager.merge(persona);
                }
                entityManager.getTransaction().commit();

                int numFilaSeleccionada;
                if (nuevaPersona)
                {
                    tableViewPrevio.getItems().add(persona);
                    numFilaSeleccionada = tableViewPrevio.getItems().size()- 1;
                    tableViewPrevio.getSelectionModel().select(numFilaSeleccionada);
                    tableViewPrevio.scrollTo(numFilaSeleccionada);
                } 
                else 
                {
                    numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
                    tableViewPrevio.getItems().set(numFilaSeleccionada,persona);
                }
                TablePosition pos = new TablePosition(tableViewPrevio,numFilaSeleccionada,null);
                tableViewPrevio.getFocusModel().focus(pos);
                tableViewPrevio.requestFocus();

                StackPane rootMain = (StackPane) rootInsertarPersonaView.getScene().getRoot();
                rootMain.getChildren().remove(rootInsertarPersonaView);
                rootAgendaView.setVisible(true);
            } 
            catch (RollbackException ex) 
            { 
                // Los datos introducidos no cumplen requisitos de BD
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. "
                        + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) 
    {
        persona.setNombre(fieldInsertarPersonaNombre.getText());
        persona.setApellidos(fieldInsertarPersonaApellidos.getText());
        persona.setTelefono(fieldInsertarPersonaTlfno.getText());
        persona.setEmail(fieldInsertarPersonaEmail.getText());
        // Falta implementar el código para el resto de controles 
        
        if (nuevaPersona)
        {
            entityManager.persist(persona);
        } 
        else 
        {
            entityManager.merge(persona);
        }        
        entityManager.getTransaction().rollback();
        
        int numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio,numFilaSeleccionada,null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
        
        
        StackPane rootMain = (StackPane) rootInsertarPersonaView.getScene().getRoot();
        rootMain.getChildren().remove(rootInsertarPersonaView);
        rootAgendaView.setVisible(true);
    }

    @FXML
    private void onActionButtonExaminar(ActionEvent event) 
    {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if (!carpetaFotos.exists())
        {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg","*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos","*.*"));
        File file = fileChooser.showOpenDialog(rootInsertarPersonaView.getScene().getWindow());
        if (file != null)
        {
            try 
            {
                Files.copy(file.toPath(),new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                persona.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                panelFoto.setImage(image);
            } 
            catch (FileAlreadyExistsException ex)
            {
                Alert alert = new Alert(AlertType.WARNING,"Nombre de archivo duplicado");
                alert.showAndWait();
            }
            catch (IOException ex)
            {
                Alert alert = new Alert(AlertType.WARNING,"No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }  
    }

    @FXML
    private void onActionButtonSuprimirFoto(ActionEvent event) 
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresión de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen,\n" 
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija la opción deseada:");
        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar",ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener,buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar)
        {
            String imageFileName = persona.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) 
            {
                file.delete();
            }
            persona.setFoto(null);
            panelFoto.setImage(null);
        } 
        else if (result.get() == buttonTypeMantener) 
        {
            persona.setFoto(null);
            panelFoto.setImage(null);
        } 
    }
    
}
