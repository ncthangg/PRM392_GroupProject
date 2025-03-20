package api;
import java.util.List;
import model.GetServiceRes;
import retrofit2.Call;
import retrofit2.http.POST;
import model.ServiceItem;
public interface ServiceApis {
    @POST("api/repair-services/get-services")
    Call<GetServiceRes> getServices();

    class ApiResponse {
        public Data data;

        public static class Data {
            public List<ServiceItem> Data;
        }
    }
}