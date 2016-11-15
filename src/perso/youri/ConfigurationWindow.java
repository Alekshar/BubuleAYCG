package perso.youri;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationWindow {

	private JFrame frmConfiguration;
	private JSpinner spinStepAngle;
	private JSpinner spinMinError;
	private JSpinner spinMaxError;
	private JSpinner spinMaxAngle;
	private JSpinner spinMinDist;
	private JSpinner spinStepDist;
	private JSpinner spinMinAngle;
	private JSpinner spinStepError;
	private JSpinner spinMaxDist;

	/**
	 * Create the application.
	 * 
	 * @param ihm
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	public ConfigurationWindow(JFrame parent) throws JSONException, IOException {
		initialize();
		frmConfiguration.setLocationRelativeTo(parent);
		setValues();
		frmConfiguration.setVisible(true);
	}

	private void setValues() throws JSONException, IOException {
		JSONObject jobj = new JSONObject(
				ProcessUtils.fileString("conf/configuration2.json"));
		spinMinDist.setValue(jobj.getJSONObject("seuilDistance").getDouble(
				"min"));
		spinMaxDist.setValue(jobj.getJSONObject("seuilDistance").getDouble(
				"max"));
		spinStepDist.setValue(jobj.getJSONObject("seuilDistance").getDouble(
				"pas"));

		spinMinError.setValue(jobj.getJSONObject("seuilErreur")
				.getDouble("min"));
		spinMaxError.setValue(jobj.getJSONObject("seuilErreur")
				.getDouble("max"));
		spinStepError.setValue(jobj.getJSONObject("seuilErreur").getDouble(
				"pas"));

		spinMinAngle
				.setValue(jobj.getJSONObject("seuilAngle").getDouble("min"));
		spinMaxAngle
				.setValue(jobj.getJSONObject("seuilAngle").getDouble("max"));
		spinStepAngle.setValue(jobj.getJSONObject("seuilAngle")
				.getDouble("pas"));

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConfiguration = new JFrame();
		frmConfiguration.setTitle("Configuration");
		frmConfiguration.setBounds(0, 0, 365, 530);
		frmConfiguration.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmConfiguration.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel spins = new JPanel();
		frmConfiguration.getContentPane().add(spins, BorderLayout.CENTER);
		GridBagLayout gbl_spins = new GridBagLayout();
		gbl_spins.columnWidths = new int[] { 360 };
		gbl_spins.rowHeights = new int[] { 100, 100, 100 };
		gbl_spins.columnWeights = new double[] { 0.0 };
		gbl_spins.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		spins.setLayout(gbl_spins);

		JPanel toleratedDistance = new JPanel();
		GridBagConstraints gbc_toleratedDistance = new GridBagConstraints();
		gbc_toleratedDistance.fill = GridBagConstraints.BOTH;
		gbc_toleratedDistance.insets = new Insets(0, 0, 5, 5);
		gbc_toleratedDistance.gridx = 0;
		gbc_toleratedDistance.gridy = 0;
		spins.add(toleratedDistance, gbc_toleratedDistance);
		toleratedDistance.setBorder(new TitledBorder(new LineBorder(new Color(
				0, 0, 0)), "Distance tol\u00E9r\u00E9e entre les marqueurs",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		GridBagLayout gbl_toleratedDistance = new GridBagLayout();
		gbl_toleratedDistance.columnWidths = new int[] { 10, 50, 100, 10 };
		gbl_toleratedDistance.rowHeights = new int[] { 10, 0, 0, 0, 10 };
		gbl_toleratedDistance.columnWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_toleratedDistance.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		toleratedDistance.setLayout(gbl_toleratedDistance);

		JLabel lblMinDist = new JLabel("minimum");
		GridBagConstraints gbc_lblMinDist = new GridBagConstraints();
		gbc_lblMinDist.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinDist.gridx = 1;
		gbc_lblMinDist.gridy = 1;
		toleratedDistance.add(lblMinDist, gbc_lblMinDist);

		spinMinDist = new JSpinner();
		spinMinDist.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinMinDist = new GridBagConstraints();
		gbc_spinMinDist.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinMinDist.insets = new Insets(0, 0, 5, 0);
		gbc_spinMinDist.gridx = 2;
		gbc_spinMinDist.gridy = 1;
		toleratedDistance.add(spinMinDist, gbc_spinMinDist);

		JLabel lblMaxDist = new JLabel("maximum");
		GridBagConstraints gbc_lblMaxDist = new GridBagConstraints();
		gbc_lblMaxDist.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxDist.gridx = 1;
		gbc_lblMaxDist.gridy = 2;
		toleratedDistance.add(lblMaxDist, gbc_lblMaxDist);

		spinMaxDist = new JSpinner();
		spinMaxDist.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinMaxDist = new GridBagConstraints();
		gbc_spinMaxDist.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinMaxDist.insets = new Insets(0, 0, 5, 0);
		gbc_spinMaxDist.gridx = 2;
		gbc_spinMaxDist.gridy = 2;
		toleratedDistance.add(spinMaxDist, gbc_spinMaxDist);

		JLabel lblStepDist = new JLabel("\u00E9tape");
		GridBagConstraints gbc_lblStepDist = new GridBagConstraints();
		gbc_lblStepDist.insets = new Insets(0, 0, 5, 5);
		gbc_lblStepDist.gridx = 1;
		gbc_lblStepDist.gridy = 3;
		toleratedDistance.add(lblStepDist, gbc_lblStepDist);

		spinStepDist = new JSpinner();
		spinStepDist.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinStepDist = new GridBagConstraints();
		gbc_spinStepDist.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinStepDist.insets = new Insets(0, 0, 5, 0);
		gbc_spinStepDist.gridx = 2;
		gbc_spinStepDist.gridy = 3;
		toleratedDistance.add(spinStepDist, gbc_spinStepDist);

		JPanel toleratedError = new JPanel();
		GridBagConstraints gbc_toleratedError = new GridBagConstraints();
		gbc_toleratedError.fill = GridBagConstraints.BOTH;
		gbc_toleratedError.insets = new Insets(0, 0, 5, 0);
		gbc_toleratedError.gridx = 0;
		gbc_toleratedError.gridy = 1;
		spins.add(toleratedError, gbc_toleratedError);
		toleratedError.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 0)), "Marge d'erreur dans la distance entre marqueurs",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		GridBagLayout gbl_toleratedError = new GridBagLayout();
		gbl_toleratedError.columnWidths = new int[] { 10, 50, 100, 10 };
		gbl_toleratedError.rowHeights = new int[] { 10, 0, 0, 0, 10 };
		gbl_toleratedError.columnWeights = new double[] { 0.0, 0.0, 0.0,
				4.9E-324 };
		gbl_toleratedError.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		toleratedError.setLayout(gbl_toleratedError);

		JLabel lblMinError = new JLabel("minimum");
		GridBagConstraints gbc_lblMinError = new GridBagConstraints();
		gbc_lblMinError.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinError.gridx = 1;
		gbc_lblMinError.gridy = 1;
		toleratedError.add(lblMinError, gbc_lblMinError);

		spinMinError = new JSpinner();
		spinMinError.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinMinError = new GridBagConstraints();
		gbc_spinMinError.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinMinError.insets = new Insets(0, 0, 5, 5);
		gbc_spinMinError.gridx = 2;
		gbc_spinMinError.gridy = 1;
		toleratedError.add(spinMinError, gbc_spinMinError);

		JLabel lblMaxError = new JLabel("maximum");
		GridBagConstraints gbc_lblMaxError = new GridBagConstraints();
		gbc_lblMaxError.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxError.gridx = 1;
		gbc_lblMaxError.gridy = 2;
		toleratedError.add(lblMaxError, gbc_lblMaxError);

		spinMaxError = new JSpinner();
		spinMaxError.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinMaxError = new GridBagConstraints();
		gbc_spinMaxError.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinMaxError.insets = new Insets(0, 0, 5, 5);
		gbc_spinMaxError.gridx = 2;
		gbc_spinMaxError.gridy = 2;
		toleratedError.add(spinMaxError, gbc_spinMaxError);

		JLabel lblStepError = new JLabel("\u00E9tape");
		GridBagConstraints gbc_lblStepError = new GridBagConstraints();
		gbc_lblStepError.insets = new Insets(0, 0, 5, 5);
		gbc_lblStepError.gridx = 1;
		gbc_lblStepError.gridy = 3;
		toleratedError.add(lblStepError, gbc_lblStepError);

		spinStepError = new JSpinner();
		spinStepError.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinStepError = new GridBagConstraints();
		gbc_spinStepError.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinStepError.insets = new Insets(0, 0, 5, 0);
		gbc_spinStepError.gridx = 2;
		gbc_spinStepError.gridy = 3;
		toleratedError.add(spinStepError, gbc_spinStepError);

		JPanel toleratedAngle = new JPanel();
		GridBagConstraints gbc_toleratedAngle = new GridBagConstraints();
		gbc_toleratedAngle.fill = GridBagConstraints.BOTH;
		gbc_toleratedAngle.gridx = 0;
		gbc_toleratedAngle.gridy = 2;
		spins.add(toleratedAngle, gbc_toleratedAngle);
		toleratedAngle.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 0)),
				"Angle tol\u00E9r\u00E9 entre trois marqueurs successifs",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		GridBagLayout gbl_toleratedAngle = new GridBagLayout();
		gbl_toleratedAngle.columnWidths = new int[] { 10, 50, 100, 10 };
		gbl_toleratedAngle.rowHeights = new int[] { 10, 0, 0, 0, 10 };
		gbl_toleratedAngle.columnWeights = new double[] { 0.0, 0.0, 0.0,
				4.9E-324 };
		gbl_toleratedAngle.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		toleratedAngle.setLayout(gbl_toleratedAngle);

		JLabel lblMinAngle = new JLabel("minimum");
		GridBagConstraints gbc_lblMinAngle = new GridBagConstraints();
		gbc_lblMinAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinAngle.gridx = 1;
		gbc_lblMinAngle.gridy = 1;
		toleratedAngle.add(lblMinAngle, gbc_lblMinAngle);

		spinMinAngle = new JSpinner();
		spinMinAngle.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinMinAngle = new GridBagConstraints();
		gbc_spinMinAngle.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinMinAngle.insets = new Insets(0, 0, 5, 5);
		gbc_spinMinAngle.gridx = 2;
		gbc_spinMinAngle.gridy = 1;
		toleratedAngle.add(spinMinAngle, gbc_spinMinAngle);

		JLabel lblMaxAngle = new JLabel("maximum");
		GridBagConstraints gbc_lblMaxAngle = new GridBagConstraints();
		gbc_lblMaxAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxAngle.gridx = 1;
		gbc_lblMaxAngle.gridy = 2;
		toleratedAngle.add(lblMaxAngle, gbc_lblMaxAngle);

		spinMaxAngle = new JSpinner();
		spinMaxAngle.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinMaxAngle = new GridBagConstraints();
		gbc_spinMaxAngle.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinMaxAngle.insets = new Insets(0, 0, 5, 5);
		gbc_spinMaxAngle.gridx = 2;
		gbc_spinMaxAngle.gridy = 2;
		toleratedAngle.add(spinMaxAngle, gbc_spinMaxAngle);

		JLabel lblStepAngle = new JLabel("\u00E9tape");
		GridBagConstraints gbc_lblStepAngle = new GridBagConstraints();
		gbc_lblStepAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblStepAngle.gridx = 1;
		gbc_lblStepAngle.gridy = 3;
		toleratedAngle.add(lblStepAngle, gbc_lblStepAngle);

		spinStepAngle = new JSpinner();
		spinStepAngle.setModel(new SpinnerNumberModel(new Double(0), null, null,
				new Double(1)));
		GridBagConstraints gbc_spinStepAngle = new GridBagConstraints();
		gbc_spinStepAngle.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinStepAngle.insets = new Insets(0, 0, 5, 0);
		gbc_spinStepAngle.gridx = 2;
		gbc_spinStepAngle.gridy = 3;
		toleratedAngle.add(spinStepAngle, gbc_spinStepAngle);

		JPanel buttons = new JPanel();
		frmConfiguration.getContentPane().add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		buttons.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JButton btnCancel = new JButton("Annuler");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmConfiguration.dispose();
			}
		});

		JButton btnDefault = new JButton("Par d\u00E9faut");
		btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ProcessUtils.printToFile("conf/configuration2.json",
							ProcessUtils.fileString("conf/default.json"));
					frmConfiguration.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		panel.add(btnDefault);
		panel.add(btnCancel);

		JButton btnConfirm = new JButton("Modifier");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JSONObject jobj = new JSONObject();
					JSONObject jobjDist = new JSONObject();
					jobjDist.put("min", spinMinDist.getValue());
					jobjDist.put("max", spinMaxDist.getValue());
					jobjDist.put("pas", spinStepDist.getValue());
					jobj.put("seuilDistance", jobjDist);

					JSONObject jobjError = new JSONObject();
					jobjError.put("min", spinMinError.getValue());
					jobjError.put("max", spinMaxError.getValue());
					jobjError.put("pas", spinStepError.getValue());
					jobj.put("seuilErreur", jobjError);

					JSONObject jobjAngle = new JSONObject();
					jobjAngle.put("min", spinMinAngle.getValue());
					jobjAngle.put("max", spinMaxAngle.getValue());
					jobjAngle.put("pas", spinStepAngle.getValue());
					jobj.put("seuilAngle", jobjAngle);

					ProcessUtils.printToFile("conf/configuration2.json",
							jobj.toString());
					
					frmConfiguration.dispose();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		panel.add(btnConfirm);
	}

	protected JSpinner getSpinStepAngle() {
		return spinStepAngle;
	}

	public JSpinner getSpinMinError() {
		return spinMinError;
	}

	public JSpinner getSpinMaxError() {
		return spinMaxError;
	}

	public JSpinner getSpinMaxAngle() {
		return spinMaxAngle;
	}

	public JSpinner getSpinMinDist() {
		return spinMinDist;
	}

	public JSpinner getSpinStepDist() {
		return spinStepDist;
	}

	public JSpinner getSpinMinAngle() {
		return spinMinAngle;
	}

	public JSpinner getSpinStepError() {
		return spinStepError;
	}

	public JSpinner getSpinMaxDist() {
		return spinMaxDist;
	}
}
