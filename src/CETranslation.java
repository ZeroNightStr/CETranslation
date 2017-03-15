import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ZeroNight on 17/3/14.
 */
public class CETranslation extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //get selected code
        Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        String selectedString = getSelectedString(mEditor);
        try {
            translateEnglishToChinese(selectedString , mEditor , project);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

    }

    public String getSelectedString(Editor mEditor) {
        if (null == mEditor) {
            return "";
        }
        SelectionModel model = mEditor.getSelectionModel();
        final String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return "";
        }
        return selectedText;
    }

    private void translateEnglishToChinese(String selectedText , Editor editor , Project project) throws UnsupportedEncodingException {
        String selectedTextz = URLEncoder.encode(selectedText, "UTF-8");
        String baseUrl = "http://fanyi.youdao.com/openapi.do?keyfrom=ZeroCETranslation&key=1039912153&type=data&doctype=json&version=1.1&q=" + selectedTextz;
        HttpUtils.doGetAsyn(baseUrl + selectedText, new HttpUtils.CallBack() {
            public void onRequestComplete(String result) {
                Gson gson = new Gson();
                Translation translation = gson.fromJson(result, Translation.class);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //genGetterAndSetter为生成getter和setter函数部分
                        String english = translation.getTranslation().get(0).toString();
                        String[] englishSplit = english.split(" ");
                        ReplaceString(editor , englishSplit[0]);
                    }
                };
                WriteCommandAction.runWriteCommandAction(project , runnable);
            }
        });
    }

    private void ReplaceString(Editor mEditor , String resultString){
        String a = resultString;
        Document document = mEditor.getDocument();
        SelectionModel selectionModel = mEditor.getSelectionModel();
        int startOffset = selectionModel.getSelectionStart();
        int endOffset = selectionModel.getSelectionEnd();
        document.replaceString(endOffset , endOffset + 1 , resultString);
        System.out.print(resultString);
    }

}
