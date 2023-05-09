function setColorScheme(scheme) {
    const element = document.documentElement;
    element.setAttribute('data-bs-theme', scheme);
}

function getPreferredColorScheme() {
    if (window.matchMedia) {
        if(window.matchMedia('(prefers-color-scheme: dark)').matches){
            return 'dark';
        } else {
            return 'light';
        }
    }
    return 'light';
}

function updateColorScheme(){
    setColorScheme(getPreferredColorScheme());
}

if(window.matchMedia){
    const colorSchemeQuery = window.matchMedia('(prefers-color-scheme: dark)');
    colorSchemeQuery.addEventListener('change', updateColorScheme);
}

updateColorScheme();