import { Component, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { trigger, state, style, animate, transition } from '@angular/animations';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
  animations: [
    trigger('fadeInOut', [
      state('void', style({
        opacity: 0,
        transform: 'translateY(-20px)'
      })),
      transition('void <=> *', animate('300ms ease-in-out')),
    ]),
    trigger('slideInOut', [
      state('void', style({
        transform: 'translateX(30px)',
        opacity: 0
      })),
      transition('void <=> *', animate('500ms 200ms ease-out')),
    ])
  ]
})
export class ForgetPasswordComponent implements OnInit, AfterViewInit {
  @ViewChild('canvas')
  canvasRef!: ElementRef;
  forgotPasswordForm: FormGroup;
  loading = false;
  isSubmitted = false;
  errorMessage: string = '';
  successMessage: string = '';
  
  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private router: Router,
    private el: ElementRef
  ) {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    // Add particle class to body
    document.body.classList.add('has-particles');
    
    // Animate food icons
    this.initFoodIconAnimations();
  }
  
  ngAfterViewInit(): void {
    // Initialize the canvas animation
    this.initCanvasAnimation();
    
    // Add floating labels effect
    const inputs = this.el.nativeElement.querySelectorAll('input');
    inputs.forEach((input: HTMLInputElement) => {
      input.addEventListener('focus', () => {
        if (input.parentElement) {
          if (input.parentElement) {
            if (input.parentElement) {
              input.parentElement.classList.add('focused');
            }
          }
        }
      });
      
      input.addEventListener('blur', () => {
        if (input.value === '') {
          if (input.parentElement) {
            input.parentElement.classList.remove('focused');
          }
        }
      });
      
    });
  }
  
  ngOnDestroy(): void {
    // Remove particle class from body
    document.body.classList.remove('has-particles');
  }

  initFoodIconAnimations(): void {
    const foodIcons = document.querySelectorAll('.food-icon');
    foodIcons.forEach((icon: Element, index) => {
      // Set random initial positions
      const randomX = Math.random() * 100;
      const randomY = Math.random() * 100;
      const randomDelay = index * 0.8;
      
      (icon as HTMLElement).style.left = `${randomX}%`;
      (icon as HTMLElement).style.top = `${randomY}%`;
      (icon as HTMLElement).style.animationDelay = `${randomDelay}s`;
    });
  }

  initCanvasAnimation(): void {
    const canvas = this.canvasRef?.nativeElement;
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Set canvas to full window size
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    
    // Particle settings
    const particlesArray: {
      update(): unknown;
      draw(): unknown;
      x: any;
      color: any; y: any; 
}[] = [];
    const numberOfParticles = 80;
    const colors = ['#FF416C', '#FF4B2B', '#FFA07A', '#FF7F50', '#FF6347'];
    
    // Create Particle class
    class Particle {
      x: number;
      y: number;
      size: number;
      speedX: number;
      speedY: number;
      color: string;
      
      constructor() {
        this.x = Math.random() * canvas.width;
        this.y = Math.random() * canvas.height;
        this.size = Math.random() * 4 + 1;
        this.speedX = Math.random() * 2 - 1;
        this.speedY = Math.random() * 2 - 1;
        this.color = colors[Math.floor(Math.random() * colors.length)];
      }
      
      update() {
        this.x += this.speedX;
        this.y += this.speedY;
        
        if (this.x > canvas.width) this.x = 0;
        else if (this.x < 0) this.x = canvas.width;
        
        if (this.y > canvas.height) this.y = 0;
        else if (this.y < 0) this.y = canvas.height;
      }
      
      draw() {
        ctx.fillStyle = this.color;
        ctx.globalAlpha = 0.7;
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
        ctx.closePath();
        ctx.fill();
      }
    }
    
    // Create particles
    function init() {
      for (let i = 0; i < numberOfParticles; i++) {
        particlesArray.push(new Particle());
      }
    }
    
    // Animation loop
    function animate() {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      
      for (let i = 0; i < particlesArray.length; i++) {
        particlesArray[i].update();
        particlesArray[i].draw();
        
        // Connect particles with lines
        for (let j = i; j < particlesArray.length; j++) {
          const dx = particlesArray[i].x - particlesArray[j].x;
          const dy = particlesArray[i].y - particlesArray[j].y;
          const distance = Math.sqrt(dx * dx + dy * dy);
          
          if (distance < 120) {
            ctx.beginPath();
            ctx.strokeStyle = particlesArray[i].color;
            ctx.globalAlpha = 0.15;
            ctx.lineWidth = 0.5;
            ctx.moveTo(particlesArray[i].x, particlesArray[i].y);
            ctx.lineTo(particlesArray[j].x, particlesArray[j].y);
            ctx.stroke();
            ctx.closePath();
          }
        }
      }
      
      requestAnimationFrame(animate);
    }
    
    // Handle window resize
    window.addEventListener('resize', () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
    });
    
    init();
    animate();
  }

  get formControls() {
    return this.forgotPasswordForm.controls;
  }

  onSubmit(): void {
    this.isSubmitted = true;
    this.clearMessages();
    
    if (this.forgotPasswordForm.invalid) {
      // Add shake animation to invalid form
      const formElement = this.el.nativeElement.querySelector('.login-right');
      formElement.classList.add('shake');
      setTimeout(() => {
        formElement.classList.remove('shake');
      }, 500);
      return;
    }

    this.loading = true;
    const email = this.formControls['email'].value;

    // Add slight delay to showcase the loading animation
    setTimeout(() => {
      this.loginService.forgotPassword(email)
      .subscribe({
        next: (response) => {
          this.loading = false;
          this.successMessage = 'Password reset email has been sent! Please check your inbox.';
          this.forgotPasswordForm.reset();
          this.isSubmitted = false;
        },
        error: (error) => {
          this.loading = false;
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'An error occurred while processing your request. Please try again.';
          }
        }
      });
    }, 800);
  }

  clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
  
  clearError(): void {
    this.errorMessage = '';
  }
  
  clearSuccess(): void {
    this.successMessage = '';
  }
}