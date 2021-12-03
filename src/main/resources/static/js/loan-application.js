const app = Vue.createApp({
    data(){
        return{
            cliente:[],
            cuentas:[],
            idLoan:"",
            loans:[],
            paymentsLoan:"",
            amountLoan:"",
            accountLoan:"",
            interestLoan:""
        }
    },
    created(){
        axios.get("/api/clients/current")
        .then(response => {
            console.log(response)
            this.cliente = response.data
            this.cuentas = response.data.accounts.sort((a,b) => a.id>b.id ? 1 : -1)//ASC
        })
        .catch(error => console.error(error.message))

        axios.get("/api/loans")
        .then(response => {
            console.log(response)
            this.loans = response.data
        })
        .catch(error => console.error(error.message))
    },
    methods:{
        logout(){
            axios.post('/api/logout')
                .then(() => window.location.href = "/web/index.html")
                .catch(error => console.log(error.message))
        },
        formatoMoneda(dinero){   
            return new Intl.NumberFormat('es-AR', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(dinero);
        },
        applyLoan(){
            if (this.amountLoan > 0) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "You will not be able to reverse the request",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, I wish to apply for the loan'
                })
                .then((result) =>  {
                    if (result.isConfirmed) {
                        
                        axios.post("/api/loans",{id: this.idLoan, amount: this.amountLoan, payments: this.paymentsLoan, number: this.accountLoan, interest: this.interestLoan})
                        .then(
                            Swal.fire({
                                title:'Made',
                                text:'Your loan will be affected immediately.',
                                icon:'success',
                                showConfirmButton: false,
                                timer: 2000,
                                timerProgressBar: true
                            }),
                            setTimeout( () => window.location.href = "/web/accounts.html",2000))
                        .catch(() => 
                            Swal.fire({
                                title: 'Please enter a correct amount',
                                showConfirmButton: false,
                                icon: "error",
                                showCloseButton: false,
                                timer: 2000,
                                timerProgressBar: true
                            })
                        )
                    }
                })
            } else {
                Swal.fire({
                    title: 'Please enter a correct amount',
                    showConfirmButton: false,
                    icon: "error",
                    showCloseButton: false,
                    timer: 2000,
                    timerProgressBar: true
                })
            }
        }
    },
    computed:{
        paymentsFiltradas(){
            return this.loans.filter(loan => loan.id == this.idLoan)[0].payments
        },
        montoMaximo(){
            return this.loans.filter(loan => loan.id == this.idLoan)[0].maxAmount
        },
        interesLoan(){
            return this.loans.filter(loan => loan.id == this.idLoan)[0].interest
        },
        interestFiltrados(){
            return this.loans.filter(loan => loan.id == this.idLoan)[0].interest
        }
    }
})
app.mount("#app")