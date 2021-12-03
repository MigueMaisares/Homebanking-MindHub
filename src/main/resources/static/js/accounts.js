const app = Vue.createApp({
    data(){
        return {
            clientes:[],
            cuentas:[],
            prestamos:[],
            type:""
        }
    },
    created(){
        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)
                this.loadData(response);
                this.cuentas.sort((a,b) => a.id>b.id ? 1 : -1)//ASC
                this.prestamos.sort((a,b) => a.id<b.id ? 1 : -1)//DESC
            })
            .catch(error => console.log(error.message))
    },
    methods:{
        loadData(r){
            this.clientes = r.data;
            this.cuentas = r.data.accounts;
            this.prestamos = r.data.loans;
        },
        logout(){
            axios.post('/api/logout').then(() => window.location.href = "/web/index.html").catch(error => console.log(error.message))
        },
        createAccount(){
            (async () => {
                const { value: typeAccount } = await Swal.fire({
                  title: 'Select an account type',
                  input: 'radio',
                  inputOptions: {
                      'SAVING': 'Saving',
                      'CURRENT': 'Current'
                    },
                  inputValidator: (value) => {
                    if (!value) {
                      return 'You need to choose something!'
                    }
                  }
                })
                this.type = typeAccount
                axios.post("/api/clients/current/accounts", "type=" + this.type, { headers:{'content-type':'application/x-www-form-urlencoded' }})
                .then(
                    Swal.fire({
                        title:'Made',
                        text:'Your account will be created immediately',
                        icon:'success',
                        showConfirmButton: false,
                        timer: 2000,
                        timerProgressBar: true
                    }),
                    setTimeout( () => window.location.href = "/web/accounts.html", 2000))
                .catch(() => 
                    Swal.fire({
                        title: 'Selection canceled',
                        showConfirmButton: false,
                        icon: "error",
                        showCloseButton: false,
                        timer: 2000,
                        timerProgressBar: true
                    }))
            })()
        },
        formatoMoneda(dinero){    
            return new Intl.NumberFormat('es-AR', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(dinero);
        },
        formatoFecha(fecha) {
            return new Date(fecha).toLocaleDateString()
        },
        deleteAccount(id){
            Swal.fire({
                title: 'Are you sure?',
                text: "You will not be able to reverse the request",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, I want to remove the account'
            })
            .then((result) =>  {
                if (result.isConfirmed) {
                    axios.delete("/api/clients/current/accounts?id=" + id)
                    .then(
                        Swal.fire({
                            title:'Made',
                            text:'Your account will be deleted immediately',
                            icon:'success',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }),
                        setTimeout( () => window.location.href = "/web/accounts.html",2000)
                    )
                    .catch(() => 
                        Swal.fire({
                            title: 'The account must have a balance of $0',
                            showConfirmButton: false,
                            icon: "error",
                            showCloseButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        })
                    )
                }
            })
        },
        contador(){
            const options = {
                  decimalPlaces: 2,
                  separator: '.',
                  decimal: ',',
            };
            let demo = new CountUp('myTargetElement', 7018.01, options);
            if (!demo.error) {
              demo.start();
            } else {
              console.error(demo.error);
            }
        }
    }
})
app.mount("#app")