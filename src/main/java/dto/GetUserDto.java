package dto;

public class GetUserDto {
    private Ad ad;

    private Data data;

    public Ad getAd ()
    {
        return ad;
    }

    public void setAd (Ad ad)
    {
        this.ad = ad;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }
}
