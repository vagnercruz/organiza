package util;

import java.io.*;

public class LoginPersistente {
    private static final String ARQUIVO = "login.dat";

    public static void salvarLogin (int usuarioId){
        try (FileWriter fw = new FileWriter(ARQUIVO)){
            fw.write(String.valueOf(usuarioId));
        }catch (IOException e){
            System.err.println("Erro ao salvar login: " + e.getMessage());
        }
    }

    public static Integer recuperarLogin(){
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))){
            String linha = br.readLine();
            return Integer.parseInt(linha);
        }catch (IOException | NumberFormatException e){
            return null;
        }
    }

    public static void limparLogin(){
        new File(ARQUIVO).delete();
    }
}
