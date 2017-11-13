package isp.com.nooranv1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.provider.BaseColumns._ID;
import static isp.com.nooranv1.Constantes.EMAIL_USUARIO;
import static isp.com.nooranv1.Constantes.FOTO_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_TABLA_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_USUARIO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public Usuario usuario;
    private static String[] FROM = {_ID, NOMBRE_USUARIO, EMAIL_USUARIO, FOTO_USUARIO};

    private BaseDatosUsuario events;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //usuario = new Usuario("pepe","pepe@gmail.com","","");
        events = new BaseDatosUsuario(this.getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        usuario = obtenerUsuario();
        TextView text = (TextView) view.findViewById(R.id.txtNombrePerfil);
        text.setText(usuario.nombre);
        TextView text1 = (TextView) view.findViewById(R.id.txtEmailPerfil);
        text1.setText(usuario.email);
        ImageView img = (ImageView) view.findViewById(R.id.imgPerfil);
        Picasso.with(getContext()).load("http://res.cloudinary.com/nooran/image/upload/v1510432969/clave1234.jpg").resize(500, 500).error(R.layout.fragment_perfil).transform(new Circulo()).into(img);
        //return inflater.inflate(R.layout.fragment_perfil, container, false);

        //llenarCamposPerfil(usuario);
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Notificacion Perfil", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void llenarCamposPerfil(Usuario u) {


        TextView text = (TextView) getView().findViewById(R.id.txtNombrePerfil);
        text.setText(u.nombre);
        TextView text1 = (TextView) getView().findViewById(R.id.txtEmailPerfil);
        text1.setText(u.email);
    }


    private Usuario obtenerUsuario() {
        SQLiteDatabase db = events.getReadableDatabase();
        Cursor cursor = db.query(NOMBRE_TABLA_USUARIO, FROM, null, null, null, null, null);
        //startManagingCursor(cursor);
        //Usuario[] usu = new Usuario[cursor.getCount()];
        Usuario u = new Usuario();
        int i = 0;

        while (cursor.moveToNext()) {
            u = new Usuario();
            u.nombre = cursor.getString(1);
            u.email = cursor.getString(2);
            u.foto = cursor.getString(3);

            //usu[i] = u;
            i++;
        }
        return u;
    }
}
