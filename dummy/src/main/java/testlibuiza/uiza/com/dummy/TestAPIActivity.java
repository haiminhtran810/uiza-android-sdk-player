package testlibuiza.uiza.com.dummy;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.listallentity.JsonBodyListAllEntity;
import vn.loitp.restapi.uiza.model.v2.listallentity.ListAllEntity;
import vn.loitp.restapi.uiza.model.v2.listallentityrelation.JsonBodyListAllEntityRelation;
import vn.loitp.restapi.uiza.model.v2.listallentityrelation.ListAllEntityRelation;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata;
import vn.loitp.restapi.uiza.model.v2.search.JsonBodySearch;
import vn.loitp.restapi.uiza.model.v2.search.Search;
import vn.loitp.rxandroid.ApiSubscriber;

public class TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
        findViewById(R.id.bt_get_token).setOnClickListener(this);
        findViewById(R.id.bt_check_token).setOnClickListener(this);
        findViewById(R.id.bt_list_metadata).setOnClickListener(this);
        findViewById(R.id.bt_search).setOnClickListener(this);
        findViewById(R.id.bt_list_entity).setOnClickListener(this);
        findViewById(R.id.bt_get_detail_entity).setOnClickListener(this);
        findViewById(R.id.bt_entity_ralation).setOnClickListener(this);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_test_api;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_token:
                auth();
                break;
            case R.id.bt_check_token:
                checkToken();
                break;
            case R.id.bt_list_metadata:
                listMetadata();
                break;
            case R.id.bt_search:
                search();
                break;
            case R.id.bt_list_entity:
                listEntity();
                break;
            case R.id.bt_get_detail_entity:
                getDetailEntity();
                break;
            case R.id.bt_entity_ralation:
                getListAllEntityRelation();
                break;
        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void auth() {
        UizaService service = RestClientV2.createService(UizaService.class);
        String accessKeyId = "Y0ZW0XM7HZL2CB8ODNDV";
        String secretKeyId = "qtQWc9Ut1SAfWK2viFJHBgViYCZYthSTjEJMlR9S";

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                showTv(auth);
                RestClientV2.addAuthorization(auth.getData().getToken());
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void checkToken() {
        UizaService service = RestClientV2.createService(UizaService.class);
        subscribe(service.checkToken(), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                showTv(auth);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void listMetadata() {
        UizaService service = RestClientV2.createService(UizaService.class);
        int limit = 999;
        String orderBy = "name";
        String orderType = "ASC";
        JsonBodyMetadataList jsonBodyMetadataList = new JsonBodyMetadataList();
        jsonBodyMetadataList.setLimit(limit);
        jsonBodyMetadataList.setOrderBy(orderBy);
        jsonBodyMetadataList.setOrderType(orderType);
        subscribe(service.listAllMetadataV2(jsonBodyMetadataList), new ApiSubscriber<ListAllMetadata>() {
            @Override
            public void onSuccess(ListAllMetadata listAllMetadata) {
                showTv(listAllMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void search() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodySearch jsonBodySearch = new JsonBodySearch();
        jsonBodySearch.setKeyword("lol");
        jsonBodySearch.setLimit(50);
        jsonBodySearch.setPage(0);

        subscribe(service.searchEntityV2(jsonBodySearch), new ApiSubscriber<Search>() {
            @Override
            public void onSuccess(Search search) {
                showTv(search);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void listEntity() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyListAllEntity jsonBodyListAllEntity = new JsonBodyListAllEntity();
        //jsonBodyListAllEntity.setMetadataId(metadataId);
        jsonBodyListAllEntity.setLimit(50);
        jsonBodyListAllEntity.setPage(0);
        jsonBodyListAllEntity.setOrderBy("createdAt");
        jsonBodyListAllEntity.setOrderType("DESC");
        subscribe(service.listAllEntityV2(jsonBodyListAllEntity), new ApiSubscriber<ListAllEntity>() {
            @Override
            public void onSuccess(ListAllEntity listAllEntity) {
                showTv(listAllEntity);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void getDetailEntity() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId("5bd11904-07b8-4859-bdc8-9fee0b2199b2");
        subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntityV2) {
                showTv(getDetailEntityV2);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void getListAllEntityRelation() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyListAllEntityRelation jsonBodyListAllEntityRelation = new JsonBodyListAllEntityRelation();
        jsonBodyListAllEntityRelation.setId("5bd11904-07b8-4859-bdc8-9fee0b2199b2");
        subscribe(service.getListAllEntityRalationV2(jsonBodyListAllEntityRelation), new ApiSubscriber<ListAllEntityRelation>() {
            @Override
            public void onSuccess(ListAllEntityRelation listAllEntityRelation) {
                showTv(listAllEntityRelation);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }
}
