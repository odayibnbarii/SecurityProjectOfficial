package com.example.securityprojectofficial.Security;
import com.example.securityprojectofficial.users.BlindUser;
import com.example.securityprojectofficial.users.FriendUser;
import com.example.securityprojectofficial.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CipherDatabase {

    private String KEY = "SEINGEYE";
    private List<String> kiList = new ArrayList<String>() ;

    public CipherDatabase(){


        KEY = PC1Key(ConvertToBinary(KEY,8));
        String key = KEY;
        for (int round=1; round<=16 ; round++){
            key = Transform(key,round);
            kiList.add(key);
        }
    }

    /**
     * encrypt user object fields.
     * @param user: user object to encrypt fields.
     * @return : encrypted object.
     */
   public User encryptUser(User user){
        String eFname = encrypt(user.getfName());
        String eLname = encrypt(user.getlName());
        String ePhone = encrypt(user.getPhone());
        String eUserType = encrypt(user.getUserType());
        String ePassword = encrypt(user.getPassword());

        if(user.getUserType().equals("BlindUser")){
            return new BlindUser(ePhone,ePassword,eFname,eLname,eUserType);
        }
        else{
            return new FriendUser(ePhone,ePassword,eFname,eLname,eUserType);
        }
    }

    /**
     * decrypt user object fields.
     * @param user : user object to decrypt fields.
     * @return : decrypted object.
     */
    public User decryptUser(User user){
        String eFname = decrypt(user.getfName());
        String eLname = decrypt(user.getlName());
        String ePhone = decrypt(user.getPhone());
        String eUserType = decrypt(user.getUserType());
        String ePassword = decrypt(user.getPassword());

        if (eUserType.equals("BlindUser")){
            return new BlindUser(ePhone,ePassword,eFname,eLname,eUserType);
        }
        else{
            return new FriendUser(ePhone,ePassword,eFname,eLname,eUserType);
        }
    }

    /**
     * Convert a String text to binary value char by char.
     * @param plaintext : String to convert
     * @param bits : bits for char.
     * @return : Binary String.
     */
    private String ConvertToBinary(String plaintext,int bits){
        String binary = "";
        for(char C : plaintext.toCharArray()){
            String conv = Integer.toBinaryString((int)C);
            int len = bits-conv.length();
            while (len>0){
                conv = "0"+conv;
                len--;
            }
            binary += conv;
        }

        return binary;
    }

    /**
     * first step to CipherDatabase algorithm is to change each bits index.
     * @param binarytext : binary text.
     * @return : binary String after changing.
     */
    private String initialPermutaion(String binarytext){
        int[] IP = new int[]{
                58,50,42,34,26,18,10,2,
                60,52,44,36,28,20,12,4,
                62,54,46,38,30,22,14,6,
                64,56,48,40,32,24,16,8,
                57,49,41,33,25,17,9 ,1,
                59,51,43,35,27,19,11,3,
                61,53,45,37,29,21,13,5,
                63,55,47,39,31,23,15,7};
        String tmp = "";
        for ( int i : IP)
            tmp += binarytext.charAt(i-1);

        return tmp;
    }

    /**
     * last step for CipherDatabase algorithm is to change bits after all the 16 rounds.
     * @param binarytext
     * @return: Ready cipher text.
     */
    private String finalPermutation(String binarytext){
        int [] IP = new int[]{
                40,8,48,16,56,24,64,32,
                39,7,47,15,55,23,63,31,
                38,6,46,14,54,22,62,30,
                37,5,45,13,53,21,61,29,
                36,4,44,12,52,20,60,28,
                35,3,43,11,51,19,59,27,
                34,2,42,10,50,18,58,26,
                33,1, 41,9,49,17,57,25};
        String tmp = "";
        for (int i : IP)
            tmp += binarytext.charAt(i-1);

        return tmp;
    }


    private String Expansion(String R){
        int[] E = new int[]{
                32,1 ,2 , 3, 4, 5,
                4 ,5 , 6, 7, 8, 9,
                8 , 9,10,11,12,13,
                12,13,14,15,16,17,
                16,17,18,19,20,21,
                20,21,22,23,24,25,
                24,25,26,27,28,29,
                28,29,30,31,32, 1} ;

        String tmp = "";
        for (int i : E)
            tmp += R.charAt(i-1);

        return tmp;
    }

    /**
     * SBOXES for the CipherDatabase f function.
     * @param R : Right half 32 bits.
     * @param i : number of the SBOX to use.
     * @return : SBOX permutation result.
     */
    private String SBox(String R,int i){
        String row  = "";
        row += String.valueOf(R.charAt(0));
        row += String.valueOf(R.charAt(5));
        String column = R.substring(1,5);
        int index = (Integer.parseInt(row,2))*16+(Integer.parseInt(column,2));
        int[] S1 = new int[]{
                14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7,
                0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,
                4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,
                15,12,8, 2,4,9,1,7,5,11,3,14,10,0,6,13};

        int[] S2 = new int[]{
                15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,
                3, 13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,
                0,14,7, 11,10,4,13,1,5,8,12,6,9,3,2,15,
                13,8,10,1,3, 15,4,2,11,6,7,12,0,5,14,9};
        int[] S3 = new int[]{
                10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,
                13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,
                13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,
                1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12};
        int[] S4 = new int[]{
                7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,
                13,8,11,5,6, 15,0,3,4,7,2,12,1,10,14,9,
                10,6,9,0,12,11,7,13,15,1, 3,14,5,2,8,4,
                3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14};
        int[] S5 = new int[]{
                2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,
                14,11,2,12,4, 7,13,1,5,0,15,10,3,9,8,6,
                4,2,1,11,10,13,7,8,15,9,12, 5,6,3,0,14,
                11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3};
        int[] S6 = new int[]{
                12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,
                10,15,4,2,7,12, 9,5,6,1,13,14,0,11,3,8,
                9,14,15,5,2,8,12,3,7,0,4,10,1, 13,11,6,
                4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13};
        int[] S7 = new int[]{
                4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,
                13,0,11,7,4,9,1, 10,14,3,5,12,2,15,8,6,
                1,4,11,13,12,3,7,14,10,15,6,8,0,5, 9,2,
                6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12};
        int[] S8 = new int[]{
                13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,
                1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,
                7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,
                2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11};
        int value =0;
        if(i==1) value = S1[index];
        if(i==2) value = S2[index];
        if(i==3) value = S3[index];
        if(i==4) value = S4[index];
        if(i==5) value = S5[index];
        if(i==6) value = S6[index];
        if(i==7) value = S7[index];
        if(i==8) value = S8[index];

        return ConvertToBinary(String.valueOf(value),4);
    }


    /**
     * permutation as a last step for the CipherDatabase f function , change bits indexes.
     * @param R
     * @return : result of the f function.
     */
    private String functionPermutation(String R ){
        int[] P = new int[]{
                16,7 ,20,21,29,12,28,17,
                1 ,15,23,26,5 ,18,31,10,
                2 ,8 ,24,14,32,27,3 ,9 ,
                19,13,30,6 ,22,11,4 ,25};
        String tmp = "";
        for(int index : P)
            tmp += R.charAt(index-1);

        return tmp;

    }

    /**
     * first permutation for the key.
     * @param key : 64-bit key.
     * @return : 56-bit key.
     */
    private String PC1Key(String key){
        int[] PC = new int[]{
                57,49,41,33,25,17,9 ,1 ,
                58,50,42,34,26,18,10,2 ,
                59,51,43,35,27,19,11,3 ,
                60,52,44,36,63,55,47,39,
                31,23,15,7 ,62,54,46,38,
                30,22,14,6 ,61,53,45,37,
                29,21,13,5 ,28,20,12,4};
        String tmp = "";
        for(int index : PC)
            tmp += key.charAt(index-1);

        return tmp;
    }

    /**
     * get the result each Transform round to permutation
     * @param key : 56-bit.
     * @return : 56-bit key that will be sent to the F function as a ki ( i : round number ).
     */
    private String PC2Key(String key){
        int[] PC = new int[]{
                14,17,11,24,1 ,5 ,3 ,28,
                15,6 ,21,10,23,19,12,4 ,
                26,8 ,16,7 ,27,20,13,2 ,
                41,52,31,37,47,55,30,40,
                51,45,33,48,44,49,39,56,
                34,53,46,42,50,36,29,32};
        String tmp = "";
        for (int index : PC)
            tmp += key.charAt(index -1);

        return tmp;
    }

    /**
     * String left rotation.
     * @param input : string to rotate.
     * @param d : Number of steps to rotate.
     * @return : Rotated string.
     */
    private String lefRotate(String input,int d){
        return input.substring(d)+input.substring(0,d);
    }

    /**
     * String right rotation.
     * @param input : String to rotate.
     * @param d : Number of steps to rotate
     * @return : Rotated String .
     */
    private String rightRotate(String input, int d){
        return input.substring(input.length()-d)+input.substring(0,input.length()-d);
    }

    /**
     * key round Transform, will called 16 time.
     * @param key : 56-bit key.
     * @param round : Round number.
     * @return : Transformed 56-bit Key.
     */
    private String Transform(String key,int round){
        String L = key.substring(0,key.length()/2);
        String R = key.substring(key.length()/2);
        int[] list = new int[]{1,2,9,16};
        int d = 2;
        for (int i : list){
            if(i == round)
                d = 1;
        }
        L = lefRotate(L,d);
        R = lefRotate(R,d);

        return L+R;
    }

    /**
     * XOR for two binary Strings.
     * @param a : first binary string.
     * @param b : second binary string.
     * @return : result of the xor.
     */
    private String XOR(String a,String b){
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<a.length() ; i++)
            sb.append(a.charAt(i)^b.charAt(i));

        return sb.toString();
    }

    /**
     * CipherDatabase f function.
     * @param R : Right half 32-bit
     * @param Ki : i round 56-bit.
     * @return
     */
    private String F(String R, String Ki){
        R = Expansion(R);
        String XORed = XOR(R,Ki);
        String S = "";
        for (int i=1 ; i<=8 ; i++){
            S += SBox(XORed.substring(0,6),i);
            XORed = XORed.substring(6);
        }

        return functionPermutation(S);
    }

    /**
     * encrypt 64-bit String.
     * @param plaintext : 64-bit binary string.
     * @return : encrypted 64-bit binary String.
     */
    public String FirstStep(String plaintext){
        String LR = initialPermutaion(plaintext);
        String L = LR.substring(0,LR.length()/2);
        String R = LR.substring(LR.length()/2);
        for (int round = 1 ; round <= 16 ; round++) {
            String Ki = PC2Key(kiList.get(round - 1));
            String tmp = L;
            L = R;
            R = XOR(tmp, F(R, Ki));
        }

        return finalPermutation(R+L);
    }

    /**
     * divide String to 8-byte blocks, convert blocks to binary blocks, encrypt each block.
     * @param plaintext : Long text to encrypt.
     * @return : Long encrypted binary string.
     */
    private String encrypt(String plaintext) {
        ArrayList<String> Blocks = new ArrayList<String>();
        while (plaintext.length() > 8) {
            Blocks.add(ConvertToBinary(plaintext.substring(0, 8), 8));
            plaintext = plaintext.substring(8);
        }
        String check = ConvertToBinary(plaintext, 8);
        int len = check.length();
        for (int i = 0; i < 64 - len; i++) {
            check = "0" + check;
        }

        Blocks.add(check);
        String ciphertext = "";
        for (String block : Blocks)
            ciphertext += FirstStep(block);
        return ciphertext;
    }

    /*********************************************/
    /**
     * convert binary string to hex String.
     * @param binary
     * @return
     */
    private String BINtoHEX(String binary){
        String hex = "";
        String convert;
        int decimal;
        while(binary.length()>0){
            convert = binary.substring(0,8);
            binary = binary.substring(8);
            decimal = Integer.parseInt(convert,2);
            hex += Integer.toString(decimal,16);
        }
        return hex;
    }
    /********************************************/

    /**
     * decrypt 64-bit binary string.
     * @param ciphertext :  64-bit encrypted string.
     * @return : decrypted and converted binary string.
     */
    private String LastStep(String ciphertext ) {
        String LR = initialPermutaion(ciphertext);
        String L = LR.substring(0, LR.length() / 2);
        String R = LR.substring(LR.length() / 2);
        for (int round = 16; round > 0; round--) {
            String Ki = PC2Key(kiList.get(round - 1));
            String tmp = L;
            L = R;
            R = XOR(tmp, F(R, Ki));
        }
        return BINtoTEXT(finalPermutation(R + L));
    }

    /**
     * divide ciphertext to 64-bits blocks and decrypt each block.
     * @param ciphertext : Long encrypted binary string.
     * @return : decrypted plaintext.
     */
    private String decrypt(String ciphertext){
        ArrayList<String> Blocks = new ArrayList<String>();
        while(ciphertext.length()>64){
            Blocks.add(ciphertext.substring(0,64));
            ciphertext = ciphertext.substring(64);
        }

        Blocks.add(ciphertext);
        String plaintext = "";
        for (String block : Blocks)
            plaintext += LastStep(block);
        return plaintext;
    }


    /**
     * convert binary string to real text ascii
     * @param binary : binary string.
     * @return : real text ascii.
     */
    private String BINtoTEXT(String binary){
        String plaintext = "";
        String ignore = "00000000";
        char nextChar;
        for (int i=1;i<=8;i++){
            String get = binary.substring(0,8);
            if (!get.equals(ignore)){
                nextChar = (char)Integer.parseInt(get,2);
                plaintext += nextChar;

            }
            binary = binary.substring(8);
        }
        return plaintext;
    }

}

