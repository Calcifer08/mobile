    package ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel;
    
    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MediatorLiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModel;
    
    import java.util.List;
    
    import ru.mirea.lozhnichenkoas.data.repositories.CurrencyRepositoryImpl;
    import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
    import ru.mirea.lozhnichenkoas.domain.models.Currency;
    import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
    import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
    import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;
    import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;
    import ru.mirea.lozhnichenkoas.domain.usecases.AddFavoriteCurrencyUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.AutoSignInUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.GetAllCurrenciesUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.GetFavoriteCurrenciesUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.LogoutUserUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.RemoveFavoriteCurrencyUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.SearchCurrencyByCodeUseCase;
    import ru.mirea.lozhnichenkoas.domain.usecases.SearchCurrencyByVoiceUseCase;
    
    public class MainViewModel extends ViewModel {
        private MutableLiveData<String> userNameLiveData = new MutableLiveData<>();
        private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
        private MutableLiveData<List<Currency>> currenciesAllLiveData = new MutableLiveData<>();
        private MutableLiveData<List<Currency>> currenciesFavoriteLiveData = new MutableLiveData<>();
        private MediatorLiveData<List<Currency>> currenciesMediatorLiveData = new MediatorLiveData<>();
    
    
        private UserRepository userRepository;
        private CurrencyRepository currencyRepository;
        private FavoriteCurrencyDao favoriteCurrencyDao;
    
        private AutoSignInUseCase autoSignInUseCase;
        private LogoutUserUseCase logoutUserUseCase;
        private GetAllCurrenciesUseCase getAllCurrenciesUseCase;
        //private GetCurrencyDetailsUseCase getCurrencyDetailsUseCase;
        private GetFavoriteCurrenciesUseCase getFavoriteCurrenciesUseCase;
        private SearchCurrencyByCodeUseCase searchCurrencyByCodeUseCase;
        private SearchCurrencyByVoiceUseCase searchCurrencyByVoiceUseCase;
    
        // fragment
        private AddFavoriteCurrencyUseCase addFavoriteCurrencyUseCase;
        private RemoveFavoriteCurrencyUseCase removeFavoriteCurrencyUseCase;
    
    
        public MainViewModel(UserRepository userRepository,
                             FavoriteCurrencyDao favoriteCurrencyDao) {
            this.userRepository = userRepository;
            this.favoriteCurrencyDao = favoriteCurrencyDao;
    
            initializeUseCases();
            initializeMediator();
        }
    
        private void initializeUseCases() {
            autoSignInUseCase = new AutoSignInUseCase(userRepository);
            logoutUserUseCase = new LogoutUserUseCase(userRepository);
    
            currencyRepository = new CurrencyRepositoryImpl(favoriteCurrencyDao);
            getAllCurrenciesUseCase = new GetAllCurrenciesUseCase(currencyRepository);
            //getCurrencyDetailsUseCase = new GetCurrencyDetailsUseCase(currencyRepository);
            getFavoriteCurrenciesUseCase = new GetFavoriteCurrenciesUseCase(currencyRepository);
            searchCurrencyByCodeUseCase = new SearchCurrencyByCodeUseCase(currencyRepository);
            searchCurrencyByVoiceUseCase = new SearchCurrencyByVoiceUseCase(currencyRepository);
    
            // fragment
            addFavoriteCurrencyUseCase = new AddFavoriteCurrencyUseCase(currencyRepository);
            removeFavoriteCurrencyUseCase = new RemoveFavoriteCurrencyUseCase(currencyRepository);
        }
    
    //    public LiveData<List<Currency>> getCurrenciesAllLiveData() {
    //        return currenciesAllLiveData;
    //    }
    //
    //    public LiveData<List<Currency>> getCurrenciesFavoriteLiveData() {
    //        return currenciesFavoriteLiveData;
    //    }
    
        public LiveData<List<Currency>> getCurrenciesMediatorLiveData() {
            return currenciesMediatorLiveData;
        }
    
        public LiveData<String> getUserNameLiveData() {
            return userNameLiveData;
        }
    
        public LiveData<String> getErrorLiveData() {
            return errorLiveData;
        }
    
        public void autoSign() {
            autoSignInUseCase.execute(new AuthCallback() {
                @Override
                public void onSuccess(String email) {
                    userNameLiveData.setValue(email);
                }
    
                @Override
                public void onFailure(String errorMessage) {
                    userNameLiveData.setValue(null);
                }
            });
        }
    
        public void logout(){
            logoutUserUseCase.execute();
            userNameLiveData.setValue(null);
        }
    
        public void resultLogin(String email){
            if (email != null && !email.isEmpty()) {
                userNameLiveData.setValue(email);
            } else {
                userNameLiveData.setValue(null);
            }
        }
    
        public void getAllCurrencies(){
            getAllCurrenciesUseCase.execute(createCallback(currenciesAllLiveData));
        }
    
        public void showFavoriteCurrencies(){
            getFavoriteCurrenciesUseCase.execute(createCallback(currenciesFavoriteLiveData));
            //currenciesFavoriteLiveData.setValue(currencyList);
        }
    
        public void initializeMediator(){
            currenciesMediatorLiveData.addSource(currenciesAllLiveData, allCurrency -> {
                if (allCurrency != null) {
                    currenciesMediatorLiveData.setValue(allCurrency);
                }
            });
    
            currenciesMediatorLiveData.addSource(currenciesFavoriteLiveData, favoriteCurrency -> {
                if (favoriteCurrency != null) {
                    currenciesMediatorLiveData.setValue(favoriteCurrency);
                }
            });
        }
    
        public void showCurrencyByCode(String charCode){
            searchCurrencyByCodeUseCase.execute(charCode, createCallback(currenciesAllLiveData));
            //currenciesAllLiveData.setValue(currencyList);
        }
    
        public void showCurrencyByVoice(){
            //заглушка  createCallback(currenciesAllLiveData)
        }
    
    
        // fragment
        public List<String> getFavoriteCurrenciesName(){
            return currencyRepository.getFavoriteCurrencies();
        }
    
        public void addToFavorites(String CharCode){
            addFavoriteCurrencyUseCase.execute(CharCode);
        }
    
        public void deleteFromFavorites(String CharCode){
            removeFavoriteCurrencyUseCase.execute(CharCode);
        }
    
    
    
        private <T> NetworkCallback<T> createCallback(MutableLiveData<T> mutableLiveData) {
            return new NetworkCallback<T>() {
                @Override
                public void onResponse(T result) {
                    mutableLiveData.setValue(result);
                }
    
                @Override
                public void onFailure(Throwable throwable) {
                    errorLiveData.setValue(throwable.getMessage());
                }
            };
        }
    }
