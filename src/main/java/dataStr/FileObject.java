package dataStr;

import java.io.Serializable;

public class FileObject  implements Serializable {
    public String url;
    public String data;
    public void FileObject(){

    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setData(String data){
        this.data = data;
    }
    public String getUrl(){
        return this.url;
    }
    public String getData(){
        return this.data;
    }
    @Override
    public String toString(){
        return this.url + this.data;
    }
}
