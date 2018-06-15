package com.grepix.grepixutils;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by devin on 2017-10-12.
 */

public class Validations {

    static Boolean requirecheck1 = false;
    static Boolean requirecheck2 = false;
    static Boolean requirecheck3 = false;

    static Boolean mrequirecheck1 = false;
    static Boolean mrequirecheck2 = false;
    static Boolean mrequirecheck3 = false;

    public static boolean isValdateSignIn(Context context, EditText Email, EditText Psw) {
        boolean isValid=true;
        if(GrepixUtils.net_connection_check(context)){
            if (Email.getText().toString().equals("")) {
                Email.setError(context.getResources().getString(R.string.required));
                isValid=false;
            } else if (Psw.getText().toString().equals("")) {
                Psw.setError(context.getResources().getString(R.string.required));
                isValid=false;
            } else if (!validEmail(Email.getText().toString())) {
                Email.setError(context.getResources().getString(R.string.invalid_email_id));
                isValid=false;
            }
        }else {
            isValid=false;
        }

        return isValid;
    }


    public static boolean isValidateRegister(Context context, EditText email, EditText mobileNo, EditText password, EditText repassword, EditText name) {
        boolean isValid = true;
        if(GrepixUtils.net_connection_check(context)){
            if (! validEmail(email.getText().toString())) {
                email.setError(context.getResources().getString(R.string.invalid_email_id));
                isValid = false;
            } else {
                email.setError(null);
            }

            if (mobileNo.getText().toString().length() < 6||mobileNo.getText().toString().length()>12) {
                isValid = false;
                mobileNo.setError(context.getResources().getString(R.string.please_enter_your_valid_number));
            } else {
                mobileNo.setError(null);
            }


            if (password.getText().toString().length() < 4) {
                isValid = false;
                password.setError(context.getResources().getString(R.string.password_lenght_should_be_alleast));
            } else {
                password.setError(null);
            }

            if (repassword.getText().toString().equals(password.getText().toString())) {
                repassword.setError(null);
            } else {
                isValid = false;
                repassword.setError(context.getResources().getString(R.string.password_not_match));
            }

            if (name.getText().toString().length() == 0) {
                name.setError(context.getResources().getText(R.string.required));
                isValid = false;
            } else {
                name.setError(null);
            }
        }else{
            isValid=false;
        }

        return isValid;
    }


    public static boolean isVaildateEditProfile(Context context, EditText firstName, EditText lastName, EditText mobile, EditText oldPassword, EditText newPassword, EditText confirmPassword, String oldPas, boolean isPasswordChange) {
        boolean isVaild = true;
        if(GrepixUtils.net_connection_check(context)){
            if (firstName.getText().toString().length() == 0) {
                firstName.setError(context.getResources().getString(R.string.required));
                isVaild = false;
            } else {
                firstName.setError(null);
            } if (lastName.getText().toString().length() == 0) {
                lastName.setError(context.getResources().getString(R.string.required));
                isVaild = false;
            } else {
                lastName.setError(null);
            } if (mobile.getText().toString().length() == 0) {
                mobile.setError(context.getResources().getString(R.string.required));
                isVaild = false;
            } else {
                mobile.setError(null);
            }

            if(isPasswordChange){
                if (oldPassword.getText().toString().length() == 0) {
                    oldPassword.setError(context.getResources().getString(R.string.enter_old_password));
                    isVaild = false;
                } else {
                    oldPassword.setError(null);
                }
                if (newPassword.getText().toString().length() == 0) {
                    newPassword.setError(context.getResources().getString(R.string.enter_new_password));
                    isVaild = false;
                } else {
                    newPassword.setError(null);
                }
                if (confirmPassword.getText().toString().length() == 0) {
                    confirmPassword.setError(context.getResources().getString(R.string.re_enter_password));
                    isVaild = false;
                } else {
                    confirmPassword.setError(null);
                }
                if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    isVaild = false;
                    confirmPassword.setError(context.getResources().getString(R.string.password_not_match));
                } else {
                    confirmPassword.setError(null);
                }

                if (!oldPassword.getText().toString().equals(oldPas)) {
                    isVaild = false;
                    oldPassword.setError(context.getResources().getString(R.string.old_password_is_not_correct));
                } else {
                    oldPassword.setError(null);
                }
            }

        }else {
            isVaild=false;
        }

        return isVaild;
    }

    public static boolean validateContactForm(Context context, EditText contact1, EditText contact2, EditText contact3) {
        boolean isValid = true;

         if(GrepixUtils.net_connection_check(context)){
             if (contact1.getText().toString().equals("") && contact2.getText().toString().equals("") && contact3.getText().toString().equals("")) {
                 // Toast.makeText(getApplicationContext(), "Atleast one Contact require", Toast.LENGTH_SHORT).show();
                 contact1.setError(context.getResources().getString(R.string.atleast_one_contact_require));
                 return false;
             } else {

                 if (!contact1.getText().toString().equals("")) {
                     if (contact1.getText().toString().length() < 6 || contact1.getText().toString().charAt(0) == '0' && contact1.getText().toString().charAt(1) == '0' && contact1.getText().toString().charAt(2) == '0') {
                         contact1.setError(context.getResources().getString(R.string.invalid_mobile_number));
                         mrequirecheck1 = false;
                         return false;
                     } else {
                         mrequirecheck1 = true;
                     }
                 } else {
                     mrequirecheck1 = true;
                 }
                 if (!contact2.getText().toString().equals("")) {
                     if (contact2.getText().toString().length() < 6 || contact1.getText().toString().charAt(0) == '0' && contact2.getText().toString().charAt(1) == '0' && contact2.getText().toString().charAt(2) == '0') {
                         contact2.setError(context.getResources().getString(R.string.invalid_mobile_number));
                         mrequirecheck2 = false;
                         isValid = false;
                     } else {
                         mrequirecheck2 = true;
                     }
                 } else {
                     mrequirecheck2 = true;
                 }
                 if (!contact3.getText().toString().equals("")) {
                     if (isVaildNumber(contact3.getText().toString())) {
                         contact3.setError(context.getResources().getString(R.string.invalid_mobile_number));
                         isValid = false;
                         mrequirecheck3 = false;
                     } else {
                         mrequirecheck3 = true;
                     }
                 } else {
                     mrequirecheck3 = true;
                 }
             }

             if (mrequirecheck1 && mrequirecheck2 && mrequirecheck3) {
                 // btUpdateEmail.setEnabled(false);
                 isValid = true;

             }
         }else{
             isValid=false;
         }

        return isValid;
    }


    public static boolean validateEmailForm(Context context, EditText email1, EditText email2, EditText email3) {

        boolean isValid = true;
       if(GrepixUtils.net_connection_check(context)){
           if (email1.getText().toString().equals("") && email1.getText().toString().equals("") && email1.getText().toString().equals("")) {
               Toast.makeText(context, R.string.atleast_one_email_required, Toast.LENGTH_SHORT).show();
           } else {
               if (!email1.getText().toString().equals("")) {
                   if (!isValidEmail(email1.getText().toString())) {
                       Toast.makeText(context, R.string.invalid_email_1_field, Toast.LENGTH_SHORT).show();
                       requirecheck1 = false;
                       return false;
                   } else {

                       requirecheck1 = true;
                   }
               } else {
                   requirecheck1 = true;
               }
               if (!email2.getText().toString().equals("")) {
                   if (!isValidEmail(email2.getText().toString())) {
                       Toast.makeText(context, R.string.invalid_email_2_field, Toast.LENGTH_SHORT).show();
                       requirecheck2 = false;
                       isValid =false;
                   } else {
                       requirecheck2 = true;
                   }
               } else {
                   requirecheck2 = true;
               }
               if (!email3.getText().toString().equals("")) {
                   if (!isValidEmail(email3.getText().toString())) {
                       Toast.makeText(context, R.string.invalid_email_3_field, Toast.LENGTH_SHORT).show();
                       requirecheck3 = false;
                       isValid =false;
                   } else {
                       requirecheck3 = true;
                   }
               } else {
                   requirecheck3 = true;
               }
               if (requirecheck1 && requirecheck2 && requirecheck3) {
                   //  btUpdateEmail.setEnabled(false);
                   isValid = true;
               }
           }
       }else {
           isValid=false;
       }




        return isValid;
    }


    public static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    public static boolean isVaildNumber(String number) {
        return number.length() < 6 ;
    }


    private static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean checkNull(String emergency) {
        try{
            if(emergency==null||emergency.equals("")||emergency.equals("null")){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            return false;
        }


    }


    public static boolean isValidateForgotPass(Context context, EditText email) {
        boolean isValid=true;
        if(isValidEmail(email.getText().toString())){

          } else {
        email.setError(null);
    }
      return isValid;
    }
}
