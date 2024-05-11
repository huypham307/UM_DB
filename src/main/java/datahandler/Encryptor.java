package datahandler;
import java.util.ArrayList;

public class Encryptor {
    private static Encryptor encryptor =null;

    public static Encryptor getInstance(){
        if(encryptor == null){
            encryptor = new Encryptor();
        }
        return encryptor;
    }

    public String encrypt(String password) {
        int[][] keyMatrix = {
            { 2,  1, 4 },
            { 0, -1, 2 },
            { 1,  3, 0 }
    };
        String encryptedText = "";
        int blockSize = password.length() / 3; // Integer division for near-equal blocks

        while(password.length() % 3 != 0){
            password = password + "X";
        }

        int start = 0;
        while(start != password.length()){
            int end = start + 3;
            String subString = password.substring(start,end);
            ArrayList columnVector = new ArrayList();
            int i = 0;
            while (i < subString.length()){
                columnVector.add((int) subString.charAt(i));
                i++;
            }
            int[] columnArray = new int[columnVector.size()];
            for (int j = 0; j < columnVector.size(); j++) {
                columnArray[j] = (int) columnVector.get(j); // Auto-unboxing to 'int'
            }

            int[][] encryptedBlock = new int[][]{matrixMultiplyVector(keyMatrix, columnArray)};
            encryptedText += convertMatrixToString(encryptedBlock);
            start = end;
        }

        return encryptedText;
    }

    private String convertMatrixToString(int[][] matrix){
        String encryptedString = "";
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                int value = matrix[i][j];
                encryptedString += (char) value;
            }
        }
        return encryptedString;
    }

    private int[] matrixMultiplyVector(int[][] matrix, int[] vector) {
        // Error Handling: Ensure matrix columns and vector length match
        int rows = matrix.length;
        int cols = matrix[0].length;

        if (cols != vector.length) {
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication.");
        }

        int[] resultVector = new int[rows]; // Initialize the result vector

        // Matrix-Vector Multiplication Logic
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resultVector[i] += matrix[i][j] * vector[j];
            }
        }

        return resultVector;
    }

    //Debug method
    public static void main(String[] args) {
        Encryptor encryptor = Encryptor.getInstance();
        String encrypted = encryptor.encrypt("Password");
        System.out.print("The encrypted password is: L" + encrypted);
    }
 
}
