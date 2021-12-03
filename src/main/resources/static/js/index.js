const app = Vue.createApp({
    data(){
        return{
            firstName:"",
            lastName:"",
            email:"",
            password:"",
            toggle:true,
        }
    },
    created(){
    },
    methods:{
        login(){
            axios.post('/api/login', "email=" + this.email + "&password=" + this.password, { headers:{'content-type':'application/x-www-form-urlencoded' }})
                .then(() => window.location.href = "/web/accounts.html")
                .catch(e => {
                    const Toast = Swal.mixin({
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                        }
                    })
                    Toast.fire({
                        icon: 'error',
                        title: 'Incorrect data'
                    })
                })
        },
        toggleSingUp(){
            this.toggle = !this.toggle;
        },
        register(){
            axios.post('/api/clients',
                "firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.email + "&password=" + this.password,
                {headers:{'content-type':'application/x-www-form-urlencoded'}}
            )
            .then(() => {
                    axios.post('/api/login',
                        "email=" + this.email + "&password=" + this.password,
                        { headers:{'content-type':'application/x-www-form-urlencoded' }}
                    )
                        .then(() => window.location.href = "/web/accounts.html")
                        .catch(e => {
                            const Toast = Swal.mixin({
                                toast: true,
                                position: 'top-end',
                                showConfirmButton: false,
                                timer: 3000,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                toast.addEventListener('mouseenter', Swal.stopTimer)
                                toast.addEventListener('mouseleave', Swal.resumeTimer)
                                }
                            })
                            Toast.fire({
                                icon: 'error',
                                title: 'Password or email wrong'
                            })
                        })
            })
            .catch(() => {
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                })
                Toast.fire({
                    icon: 'error',
                    title: 'Password or email wrong'
                })
            })
        }
    }
})
app.mount("#app")