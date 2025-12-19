// Smooth scrolling function

function scrollToSection(sectionId) {

    const element = document.getElementById(sectionId);

    if (element) {

        element.scrollIntoView({ 

            behavior: 'smooth',

            block: 'start'

        });

    }

}



// Add scroll effect to navbar

window.addEventListener('scroll', function() {

    const navbar = document.querySelector('.navbar');

    if (window.scrollY > 50) {

        navbar.style.background = 'rgba(13, 13, 13, 0.95)';

    } else {

        navbar.style.background = 'rgba(13, 13, 13, 0.9)';

    }

});



// Add click handlers for buttons

document.addEventListener('DOMContentLoaded', function() {

    // Report button click handler

    const reportButton = document.getElementById('report');

    if (reportButton) {

        reportButton.addEventListener('click', function() {

            alert('Report form would open here. This is a demo implementation.');

        });

    }



    // Process CTA button

    const processCta = document.querySelector('.process-cta .btn-primary');

    if (processCta) {

        processCta.addEventListener('click', function() {

            alert('Report form would open here. This is a demo implementation.');

        });

    }



    // Add hover effects to cards

    const threatCards = document.querySelectorAll('.threat-card');

    threatCards.forEach(card => {

        card.addEventListener('mouseenter', function() {

            this.style.transform = 'translate(-8px)';

        });

        

        card.addEventListener('mouseleave', function() {

            this.style.transform = 'translate(0)';

        });

    });



    // Add animation on scroll for process steps

    const observerOptions = {

        threshold: 0.1,

        rootMargin: '0px 0px -50px 0px'

    };



    const observer = new IntersectionObserver(function(entries) {

        entries.forEach(entry => {

            if (entry.isIntersecting) {

                entry.target.style.opacity = '1';

                entry.target.style.transform = 'translate(0)';

            }

        });

    }, observerOptions);



    // Observe process steps

    const processSteps = document.querySelectorAll('.process-step');

    processSteps.forEach((step, index) => {

        step.style.opacity = '0';

        step.style.transform = 'translate(30px)';

        step.style.transition = `opacity 0.6s ease ${index * 0.2}s, transform 0.6s ease ${index * 0.2}s`;

        observer.observe(step);

    });



    // Observe threat cards

    const threatCardsForAnimation = document.querySelectorAll('.threat-card');

    threatCardsForAnimation.forEach((card, index) => {

        card.style.opacity = '0';

        card.style.transform = 'translate(30px)';

        card.style.transition = `opacity 0.6s ease ${index * 0.1}s, transform 0.6s ease ${index * 0.1}s`;

        observer.observe(card);

    });



    // Add mobile menu toggle (for future enhancement)

    const mobileMenuButton = document.createElement('button');

    mobileMenuButton.className = 'mobile-menu-button';

    mobileMenuButton.innerHTML = `

        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">

            <line x1="3" y1="6" x2="21" y2="6"/>

            <line x1="3" y1="12" x2="21" y2="12"/>

            <line x1="3" y1="18" x2="21" y2="18"/>

        </svg>

    `;

    

    // Add mobile menu styles

    const mobileMenuStyles = `

        .mobile-menu-button {

            display: block;

            background: none;

            border: none;

            color: white;

            cursor: pointer;

            padding: 0.5rem;

        }

        

        .mobile-menu-button svg {

            width: 1.5rem;

            height: 1.5rem;

        }

        

        @media (min-width: 768px) {

            .mobile-menu-button {

                display: none;

            }

        }

    `;

    

    const stylesheet = document.createElement('style');

    stylesheet.textContent = mobileMenuStyles;

    document.head.appendChild(stylesheet);

    

    // Insert mobile menu button (hidden on desktop)

    const navLinks = document.querySelector('.nav-links');

    if (navLinks && window.innerWidth < 768) {

        navLinks.style.display = 'none';

        document.querySelector('.nav-content').appendChild(mobileMenuButton);

    }

});



// Handle window resize

window.addEventListener('resize', function() {

    const navLinks = document.querySelector('.nav-links');

    const mobileMenuButton = document.querySelector('.mobile-menu-button');

    

    if (window.innerWidth >= 768) {

        if (navLinks) navLinks.style.display = 'flex';

        if (mobileMenuButton) mobileMenuButton.style.display = 'none';

    } else {

        if (navLinks) navLinks.style.display = 'none';

        if (mobileMenuButton) mobileMenuButton.style.display = 'block';

    }

});



// Add loading animation

window.addEventListener('load', function() {

    document.body.style.opacity = '0';

    document.body.style.transition = 'opacity 0.5s ease';

    

    setTimeout(() => {

        document.body.style.opacity = '1';

    }, 100);

});



// Add parallax effect to hero section

window.addEventListener('scroll', function() {

    const scrolled = window.pageYOffset;

    const heroPattern = document.querySelector('.hero-pattern');

    

    if (heroPattern && scrolled < window.innerHeight) {

        heroPattern.style.transform = `translateY(${scrolled * 0.5}px)`;

    }

});



// Add typing effect to hero title (optional enhancement)

function typewriter(element, text, speed = 100) {

    let i = 0;

    element.innerHTML = '';

    

    function type() {

        if (i < text.length) {

            element.innerHTML += text.charAt(i);

            i++;

            setTimeout(type, speed);

        }

    }

    

    type();

}



// Uncomment to enable typing effect

// document.addEventListener('DOMContentLoaded', function() {

//     const heroTitle = document.querySelector('.hero-title');

//     if (heroTitle) {

//         const originalText = heroTitle.textContent;

//         typewriter(heroTitle, originalText, 50);

//     }

// });// Smooth scrolling function
function scrollToSection(sectionId) {
    const element = document.getElementById(sectionId);
    if (element) {
        element.scrollIntoView({ 
            behavior: 'smooth',
            block: 'start'
        });
    }
}

// Add scroll effect to navbar
window.addEventListener('scroll', function() {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.style.background = 'rgba(13, 13, 13, 0.95)';
    } else {
        navbar.style.background = 'rgba(13, 13, 13, 0.9)';
    }
});

// Add click handlers for buttons
document.addEventListener('DOMContentLoaded', function() {
    // Report button click handler
    const reportButton = document.getElementById('report');
    if (reportButton) {
        reportButton.addEventListener('click', function() {
            alert('Report form would open here. This is a demo implementation.');
        });
    }

    // Process CTA button
    const processCta = document.querySelector('.process-cta .btn-primary');
    if (processCta) {
        processCta.addEventListener('click', function() {
            alert('Report form would open here. This is a demo implementation.');
        });
    }

    // Add hover effects to cards
    const threatCards = document.querySelectorAll('.threat-card');
    threatCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Add animation on scroll for process steps
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    // Observe process steps
    const processSteps = document.querySelectorAll('.process-step');
    processSteps.forEach((step, index) => {
        step.style.opacity = '0';
        step.style.transform = 'translateY(30px)';
        step.style.transition = `opacity 0.6s ease ${index * 0.2}s, transform 0.6s ease ${index * 0.2}s`;
        observer.observe(step);
    });

    // Observe threat cards
    const threatCardsForAnimation = document.querySelectorAll('.threat-card');
    threatCardsForAnimation.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';
        card.style.transition = `opacity 0.6s ease ${index * 0.1}s, transform 0.6s ease ${index * 0.1}s`;
        observer.observe(card);
    });

    // Add mobile menu toggle (for future enhancement)
    const mobileMenuButton = document.createElement('button');
    mobileMenuButton.className = 'mobile-menu-button';
    mobileMenuButton.innerHTML = `
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="3" y1="6" x2="21" y2="6"/>
            <line x1="3" y1="12" x2="21" y2="12"/>
            <line x1="3" y1="18" x2="21" y2="18"/>
        </svg>
    `;
    
    // Add mobile menu styles
    const mobileMenuStyles = `
        .mobile-menu-button {
            display: block;
            background: none;
            border: none;
            color: white;
            cursor: pointer;
            padding: 0.5rem;
        }
        
        .mobile-menu-button svg {
            width: 1.5rem;
            height: 1.5rem;
        }
        
        @media (min-width: 768px) {
            .mobile-menu-button {
                display: none;
            }
        }
    `;
    
    const styleSheet = document.createElement('style');
    styleSheet.textContent = mobileMenuStyles;
    document.head.appendChild(styleSheet);
    
    // Insert mobile menu button (hidden on desktop)
    const navLinks = document.querySelector('.nav-links');
    if (navLinks && window.innerWidth < 768) {
        navLinks.style.display = 'none';
        document.querySelector('.nav-content').appendChild(mobileMenuButton);
    }
});

// Handle window resize
window.addEventListener('resize', function() {
    const navLinks = document.querySelector('.nav-links');
    const mobileMenuButton = document.querySelector('.mobile-menu-button');
    
    if (window.innerWidth >= 768) {
        if (navLinks) navLinks.style.display = 'flex';
        if (mobileMenuButton) mobileMenuButton.style.display = 'none';
    } else {
        if (navLinks) navLinks.style.display = 'none';
        if (mobileMenuButton) mobileMenuButton.style.display = 'block';
    }
});

// Add loading animation
window.addEventListener('load', function() {
    document.body.style.opacity = '0';
    document.body.style.transition = 'opacity 0.5s ease';
    
    setTimeout(() => {
        document.body.style.opacity = '1';
    }, 100);
});

// Add parallax effect to hero section
window.addEventListener('scroll', function() {
    const scrolled = window.pageYOffset;
    const heroPattern = document.querySelector('.hero-pattern');
    
    if (heroPattern && scrolled < window.innerHeight) {
        heroPattern.style.transform = `translateY(${scrolled * 0.5}px)`;
    }
});

// Add typing effect to hero title (optional enhancement)
function typeWriter(element, text, speed = 100) {
    let i = 0;
    element.innerHTML = '';
    
    function type() {
        if (i < text.length) {
            element.innerHTML += text.charAt(i);
            i++;
            setTimeout(type, speed);
        }
    }
    
    type();
}

// Uncomment to enable typing effect
// document.addEventListener('DOMContentLoaded', function() {
//     const heroTitle = document.querySelector('.hero-title');
//     if (heroTitle) {
//         const originalText = heroTitle.textContent;
//         typeWriter(heroTitle, originalText, 50);
//     }
// });// Smooth scrolling function
function scrollToSection(sectionId) {
    const element = document.getElementById(sectionId);
    if (element) {
        element.scrollIntoView({ 
            behavior: 'smooth',
            block: 'start'
        });
    }
}

// Add scroll effect to navbar
window.addEventListener('scroll', function() {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.style.background = 'rgba(13, 13, 13, 0.95)';
    } else {
        navbar.style.background = 'rgba(13, 13, 13, 0.9)';
    }
});

// Add click handlers for buttons
document.addEventListener('DOMContentLoaded', function() {
    // Report button click handler
    const reportButton = document.getElementById('report');
    if (reportButton) {
        reportButton.addEventListener('click', function() {
            alert('Report form would open here. This is a demo implementation.');
        });
    }

    // Process CTA button
    const processCta = document.querySelector('.process-cta .btn-primary');
    if (processCta) {
        processCta.addEventListener('click', function() {
            alert('Report form would open here. This is a demo implementation.');
        });
    }

    // Add hover effects to cards
    const threatCards = document.querySelectorAll('.threat-card');
    threatCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Add animation on scroll for process steps
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    // Observe process steps
    const processSteps = document.querySelectorAll('.process-step');
    processSteps.forEach((step, index) => {
        step.style.opacity = '0';
        step.style.transform = 'translateY(30px)';
        step.style.transition = `opacity 0.6s ease ${index * 0.2}s, transform 0.6s ease ${index * 0.2}s`;
        observer.observe(step);
    });

    // Observe threat cards
    const threatCardsForAnimation = document.querySelectorAll('.threat-card');
    threatCardsForAnimation.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';
        card.style.transition = `opacity 0.6s ease ${index * 0.1}s, transform 0.6s ease ${index * 0.1}s`;
        observer.observe(card);
    });

    // Add mobile menu toggle (for future enhancement)
    const mobileMenuButton = document.createElement('button');
    mobileMenuButton.className = 'mobile-menu-button';
    mobileMenuButton.innerHTML = `
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="3" y1="6" x2="21" y2="6"/>
            <line x1="3" y1="12" x2="21" y2="12"/>
            <line x1="3" y1="18" x2="21" y2="18"/>
        </svg>
    `;
    
    // Add mobile menu styles
    const mobileMenuStyles = `
        .mobile-menu-button {
            display: block;
            background: none;
            border: none;
            color: white;
            cursor: pointer;
            padding: 0.5rem;
        }
        
        .mobile-menu-button svg {
            width: 1.5rem;
            height: 1.5rem;
        }
        
        @media (min-width: 768px) {
            .mobile-menu-button {
                display: none;
            }
        }
    `;
    
    const styleSheet = document.createElement('style');
    styleSheet.textContent = mobileMenuStyles;
    document.head.appendChild(styleSheet);
    
    // Insert mobile menu button (hidden on desktop)
    const navLinks = document.querySelector('.nav-links');
    if (navLinks && window.innerWidth < 768) {
        navLinks.style.display = 'none';
        document.querySelector('.nav-content').appendChild(mobileMenuButton);
    }
});

// Handle window resize
window.addEventListener('resize', function() {
    const navLinks = document.querySelector('.nav-links');
    const mobileMenuButton = document.querySelector('.mobile-menu-button');
    
    if (window.innerWidth >= 768) {
        if (navLinks) navLinks.style.display = 'flex';
        if (mobileMenuButton) mobileMenuButton.style.display = 'none';
    } else {
        if (navLinks) navLinks.style.display = 'none';
        if (mobileMenuButton) mobileMenuButton.style.display = 'block';
    }
});

// Add loading animation
window.addEventListener('load', function() {
    document.body.style.opacity = '0';
    document.body.style.transition = 'opacity 0.5s ease';
    
    setTimeout(() => {
        document.body.style.opacity = '1';
    }, 100);
});

// Add parallax effect to hero section
window.addEventListener('scroll', function() {
    const scrolled = window.pageYOffset;
    const heroPattern = document.querySelector('.hero-pattern');
    
    if (heroPattern && scrolled < window.innerHeight) {
        heroPattern.style.transform = `translateY(${scrolled * 0.5}px)`;
    }
});

// Add typing effect to hero title (optional enhancement)
function typeWriter(element, text, speed = 100) {
    let i = 0;
    element.innerHTML = '';
    
    function type() {
        if (i < text.length) {
            element.innerHTML += text.charAt(i);
            i++;
            setTimeout(type, speed);
        }
    }
    
    type();
}

// Uncomment to enable typing effect
// document.addEventListener('DOMContentLoaded', function() {
//     const heroTitle = document.querySelector('.hero-title');
//     if (heroTitle) {
//         const originalText = heroTitle.textContent;
//         typeWriter(heroTitle, originalText, 50);
//     }
// });


const apiKey = "668d32946e5d44429b186412569d1d8e";
const topics = 'cybercrimes';
const query = encodeURIComponent(topics);
const url = `https://newsapi.org/v2/everything?q=${query}&language=en&sortBy=publishedAt&apiKey=${apiKey}`;

function fetchNews() {
  fetch(url)
    .then(response => response.json())
    .then(data => {
      const newsContainer = document.getElementById('news');
      newsContainer.innerHTML = ""; // Clear old news

      const articles = data.articles.slice(0, 6); // top 6
      if (articles.length === 0) {
        newsContainer.innerHTML = "<p>No recent cybercrime news found.</p>";
        return;
      }

      articles.forEach(article => {
        const card = document.createElement('div');
        card.className = 'news-card';

        // Thumbnail
        if (article.urlToImage) {
          const img = document.createElement('img');
          img.src = article.urlToImage;
          img.alt = "News image";
          card.appendChild(img);
        }

        // Headline
        const title = document.createElement('h3');
        title.textContent = article.title;
        card.appendChild(title);

        // Source and link
        const link = document.createElement('a');
        link.href = article.url;
        link.target = "_blank";
        link.textContent = "Read more →";
        card.appendChild(link);

        newsContainer.appendChild(card);
      });
    })
    .catch(error => console.error("Error fetching news:", error));
}

fetchNews();
setInterval(fetchNews, 300000);
